package com.dumbdogdiner.sass.db

import com.dumbdogdiner.sass.SassPlugin
import com.dumbdogdiner.sass.impl.stats.StatisticImpl
import com.dumbdogdiner.sass.util.fromCbor
import com.dumbdogdiner.sass.util.toCbor
import com.google.gson.JsonElement
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlLogger
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.exists
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.statements.StatementContext
import org.jetbrains.exposed.sql.statements.api.ExposedBlob
import org.jetbrains.exposed.sql.statements.expandArgs
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.util.UUID

private lateinit var db: Database
private lateinit var logger: SqlLogger

private fun Table.createIfNotExists() {
    if (!this.exists()) {
        SchemaUtils.create(this)
    }
}

fun databaseInit(database: Database) {
    db = database
    logger = object : SqlLogger {
        override fun log(context: StatementContext, transaction: Transaction) {
            SassPlugin.instance.logger.info("[SQL] ${context.expandArgs(transaction)}")
        }
    }

    // make sure the needed tables exist
    loggedTransaction {
        StatPools.createIfNotExists()
        StatMaps.createIfNotExists()
        StatEntries.createIfNotExists()
    }
}

private fun <T> loggedTransaction(statement: Transaction.() -> T) = transaction(db) {
    addLogger(logger)
    statement()
}

private val StatisticImpl.pluginName
    get() = this.store.plugin.name

private val StatisticImpl.serverName
    get() = if (this.store.isGlobal) SassPlugin.instance.serverName else null

private fun selectStatPool(stat: StatisticImpl) =
    StatPools.pluginName eq stat.pluginName and (StatPools.statName eq stat.identifier)

private fun selectStatMap(stat: StatisticImpl, statPoolId: Int) =
    StatMaps.statPoolId eq statPoolId and (StatMaps.serverName eq stat.serverName)

private fun selectStatEntries(statMapId: Int, playerId: UUID) =
    StatEntries.statMapId eq statMapId and (StatEntries.playerId eq playerId)

private fun getStatPoolAndMap(stat: StatisticImpl): Pair<Int, Int>? {
    // Check if there's a stat pool for this statistic
    val statPoolId = StatPools.select(selectStatPool(stat)).firstOrNull()?.let { it[StatPools.statPoolId] }
        ?: return null
    // Check if there's a stat map for this server
    val statMapId = StatMaps.select(selectStatMap(stat, statPoolId)).firstOrNull()?.let { it[StatMaps.statMapId] }
        ?: return null
    return statPoolId to statMapId
}

fun databaseGet(stat: StatisticImpl, playerId: UUID): JsonElement? {
    return loggedTransaction {
        val (_, statMapId) = getStatPoolAndMap(stat) ?: return@loggedTransaction null
        // Check if there's an entry for the player
        val valueBlob = StatEntries.select(selectStatEntries(statMapId, playerId)).firstOrNull()?.let { it[StatEntries.statValue] }
            ?: return@loggedTransaction null
        valueBlob.bytes.fromCbor()
    }
}

fun databaseSet(stat: StatisticImpl, playerId: UUID, value: JsonElement) {
    loggedTransaction {
        // Get a pool ID
        val statPoolId = StatPools.select(selectStatPool(stat)).firstOrNull()?.let { it[StatPools.statPoolId] }
            ?: StatPools.insert {
                it[pluginName] = stat.pluginName
                it[statName] = stat.identifier
            }[StatPools.statPoolId]
        // Get a map ID
        val statMapId = StatMaps.select(selectStatMap(stat, statPoolId)).firstOrNull()?.let { it[StatMaps.statMapId] }
            ?: StatMaps.insert {
                it[StatMaps.statPoolId] = statPoolId
                it[serverName] = stat.serverName
            }[StatMaps.statMapId]
        // Update or create an entry for the value
        val valueBlob = ExposedBlob(value.toCbor())
        val updateResult = StatEntries.update({ selectStatEntries(statMapId, playerId) }) {
            it[statValue] = valueBlob
        }
        if (updateResult == 0) {
            StatEntries.insert {
                it[StatEntries.statMapId] = statMapId
                it[StatEntries.playerId] = playerId
                it[statValue] = valueBlob
            }
        }
    }
}

fun databaseRemove(stat: StatisticImpl, playerId: UUID): Boolean {
    return loggedTransaction {
        val (statPoolId, statMapId) = getStatPoolAndMap(stat) ?: return@loggedTransaction false
        val entryDeleteResult = StatEntries.deleteWhere { selectStatEntries(statMapId, playerId) }
        if (entryDeleteResult != 0) {
            // If that was the last entry in this map, delete the map
            if (StatEntries.select { StatEntries.statMapId eq statMapId }.none()) {
                StatMaps.deleteWhere { StatMaps.statMapId eq statMapId }
                // If that was the last map in this pool, delete the pool
                if (StatMaps.select { StatMaps.statPoolId eq statPoolId }.none()) {
                    StatPools.deleteWhere { StatPools.statPoolId eq statPoolId }
                }
            }
        }
        entryDeleteResult != 0
    }
}

fun databaseReset(stat: StatisticImpl) {
    loggedTransaction {
        val (statPoolId, statMapId) = getStatPoolAndMap(stat) ?: return@loggedTransaction
        val entryDeleteResult = StatEntries.deleteWhere { StatEntries.statMapId eq statMapId }
        if (entryDeleteResult != 0) {
            StatMaps.deleteWhere { StatMaps.statMapId eq statMapId }
            // If that was the last map in this pool, delete the pool
            if (StatMaps.select { StatMaps.statPoolId eq statPoolId }.none()) {
                StatPools.deleteWhere { StatPools.statPoolId eq statPoolId }
            }
        }
    }
}