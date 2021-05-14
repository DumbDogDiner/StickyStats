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
    StatPools.pluginNameCol eq stat.pluginName and (StatPools.statNameCol eq stat.identifier)

private fun selectStatMap(stat: StatisticImpl, statPoolId: Int) =
    StatMaps.statPoolIdCol eq statPoolId and (StatMaps.serverNameCol eq stat.serverName)

private fun selectStatEntries(statMapId: Int, playerId: UUID) =
    StatEntries.statMapIdCol eq statMapId and (StatEntries.playerIdCol eq playerId)

private fun getStatPoolId(stat: StatisticImpl) = stat.cachedStatPool ?: run {
    StatPools.select(selectStatPool(stat)).singleOrNull()?.let { it[StatPools.statPoolIdCol] }
        ?.also { stat.cachedStatPool = it }
}

private fun getStatMapId(stat: StatisticImpl, statPoolId: Int) = stat.cachedStatMap ?: run {
    StatMaps.select(selectStatMap(stat, statPoolId)).singleOrNull()?.let { it[StatMaps.statMapIdCol] }
        ?.also { stat.cachedStatMap = it }
}

fun databaseGet(stat: StatisticImpl, playerId: UUID): JsonElement? {
    return loggedTransaction {
        val statMapId = getStatPoolId(stat)?.let { getStatMapId(stat, it) } ?: return@loggedTransaction null
        // Check if there's an entry for the player
        val valueBlob = StatEntries.select(selectStatEntries(statMapId, playerId)).singleOrNull()?.let { it[StatEntries.statValueCol] }
            ?: return@loggedTransaction null
        valueBlob.bytes.fromCbor()
    }
}

fun databaseSet(stat: StatisticImpl, playerId: UUID, value: JsonElement) {
    loggedTransaction {
        // Get a pool ID, or make a new one
        val statPoolId = getStatPoolId(stat)
            ?: StatPools.insert {
                it[pluginNameCol] = stat.pluginName
                it[statNameCol] = stat.identifier
            }[StatPools.statPoolIdCol].also { stat.cachedStatPool = it }
        // Get a map ID, or make a new one
        val statMapId = getStatMapId(stat, statPoolId)
            ?: StatMaps.insert {
                it[statPoolIdCol] = statPoolId
                it[serverNameCol] = stat.serverName
            }[StatMaps.statMapIdCol].also { stat.cachedStatMap = it }
        // Update or create an entry for the value
        val valueBlob = ExposedBlob(value.toCbor())
        val updateResult = StatEntries.update({ selectStatEntries(statMapId, playerId) }) {
            it[statValueCol] = valueBlob
        }
        if (updateResult == 0) {
            StatEntries.insert {
                it[statMapIdCol] = statMapId
                it[playerIdCol] = playerId
                it[statValueCol] = valueBlob
            }
        }
    }
}

fun databaseRemove(stat: StatisticImpl, playerId: UUID): Boolean {
    return loggedTransaction {
        val statPoolId = getStatPoolId(stat) ?: return@loggedTransaction false
        val statMapId = getStatMapId(stat, statPoolId) ?: return@loggedTransaction false
        val entryDeleteResult = StatEntries.deleteWhere { selectStatEntries(statMapId, playerId) }
        if (entryDeleteResult != 0) {
            // If that was the last entry in this map, delete the map
            if (StatEntries.select { StatEntries.statMapIdCol eq statMapId }.none()) {
                stat.cachedStatMap = null
                StatMaps.deleteWhere { StatMaps.statMapIdCol eq statMapId }
                // If that was the last map in this pool, delete the pool
                if (StatMaps.select { StatMaps.statPoolIdCol eq statPoolId }.none()) {
                    stat.cachedStatPool = null
                    StatPools.deleteWhere { StatPools.statPoolIdCol eq statPoolId }
                }
            }
        }
        entryDeleteResult != 0
    }
}

fun databaseReset(stat: StatisticImpl) {
    loggedTransaction {
        val statPoolId = getStatPoolId(stat) ?: return@loggedTransaction
        val statMapId = getStatMapId(stat, statPoolId) ?: return@loggedTransaction
        val entryDeleteResult = StatEntries.deleteWhere { StatEntries.statMapIdCol eq statMapId }
        if (entryDeleteResult != 0) {
            stat.cachedStatMap = null
            StatMaps.deleteWhere { StatMaps.statMapIdCol eq statMapId }
            // If that was the last map in this pool, delete the pool
            if (StatMaps.select { StatMaps.statPoolIdCol eq statPoolId }.none()) {
                stat.cachedStatPool = null
                StatPools.deleteWhere { StatPools.statPoolIdCol eq statPoolId }
            }
        }
    }
}