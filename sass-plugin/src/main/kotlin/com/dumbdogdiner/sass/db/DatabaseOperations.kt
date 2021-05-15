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

internal fun <T> loggedTransaction(statement: Transaction.() -> T) = transaction(db) {
    addLogger(logger)
    statement()
}

private fun selectStatEntries(statMapId: Int, playerId: UUID) =
    StatEntries.statMapId eq statMapId and (StatEntries.playerId eq playerId)

private fun getStatValue(statMapId: Int, playerId: UUID): ExposedBlob? {
    val row = StatEntries.select(selectStatEntries(statMapId, playerId)).singleOrNull() ?: return null
    return row[StatEntries.statValue]
}

fun databaseGet(stat: StatisticImpl, playerId: UUID): JsonElement? {
    return loggedTransaction {
        val statMapId = stat.statMapId ?: return@loggedTransaction null
        val statValue = getStatValue(statMapId, playerId) ?: return@loggedTransaction null
        statValue.bytes.fromCbor()
    }
}

private fun insertStatPool(stat: StatisticImpl) = StatPools.insert {
    it[pluginName] = stat.pluginName
    it[statName] = stat.identifier
}[StatPools.statPoolId].also { stat.statPoolIdDelegate.overrideCache(it) }

private fun insertStatMap(stat: StatisticImpl, statPoolId: Int) = StatMaps.insert {
    it[this.statPoolId] = statPoolId
    it[serverName] = stat.serverName
}[StatMaps.statMapId].also { stat.statMapIdDelegate.overrideCache(it) }

private fun insertStatEntry(statMapId: Int, playerId: UUID, statValue: ExposedBlob) = StatEntries.insert {
    it[this.statMapId] = statMapId
    it[this.playerId] = playerId
    it[this.statValue] = statValue
}

private fun updateStatEntry(statMapId: Int, playerId: UUID, statValue: ExposedBlob) =
    StatEntries.update({ selectStatEntries(statMapId, playerId )}) {
        it[this.statValue] = statValue
    }

fun databaseSet(stat: StatisticImpl, playerId: UUID, value: JsonElement) {
    loggedTransaction {
        // Get a pool ID, or make a new one
        val statPoolId = stat.statPoolId ?: insertStatPool(stat)
        // Get a map ID, or make a new one
        val statMapId = stat.statMapId ?: insertStatMap(stat, statPoolId)
        // Update or create an entry for the value
        val statValue = ExposedBlob(value.toCbor())
        val updateResult = updateStatEntry(statMapId, playerId, statValue)
        if (updateResult == 0) {
            insertStatEntry(statMapId, playerId, statValue)
        }
    }
}

fun databaseRemove(stat: StatisticImpl, playerId: UUID): Boolean {
    return loggedTransaction {
        val statMapId = stat.statMapId ?: return@loggedTransaction false
        val entryDeleteResult = StatEntries.deleteWhere { selectStatEntries(statMapId, playerId) }
        if (entryDeleteResult != 0) {
            // If that was the last entry in this map, delete the map
            if (StatEntries.select { StatEntries.statMapId eq statMapId }.none()) {
                stat.statMapIdDelegate.invalidateCache()
                StatMaps.deleteWhere { StatMaps.statMapId eq statMapId }
                // If that was the last map in this pool, delete the pool
                val statPoolId = stat.statPoolId ?: return@loggedTransaction entryDeleteResult != 0
                if (StatMaps.select { StatMaps.statPoolId eq statPoolId }.none()) {
                    stat.statPoolIdDelegate.invalidateCache()
                    StatPools.deleteWhere { StatPools.statPoolId eq statPoolId }
                }
            }
        }
        entryDeleteResult != 0
    }
}

fun databaseReset(stat: StatisticImpl) {
    loggedTransaction {
        val statMapId = stat.statMapId ?: return@loggedTransaction
        val entryDeleteResult = StatEntries.deleteWhere { StatEntries.statMapId eq statMapId }
        if (entryDeleteResult != 0) {
            stat.statMapIdDelegate.invalidateCache()
            StatMaps.deleteWhere { StatMaps.statMapId eq statMapId }
            // If that was the last map in this pool, delete the pool
            val statPoolId = stat.statPoolId ?: return@loggedTransaction
            if (StatMaps.select { StatMaps.statPoolId eq statPoolId }.none()) {
                stat.statPoolIdDelegate.invalidateCache()
                StatPools.deleteWhere { StatPools.statPoolId eq statPoolId }
            }
        }
    }
}