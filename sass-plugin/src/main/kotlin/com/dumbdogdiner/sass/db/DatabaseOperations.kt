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

fun databaseGet(stat: StatisticImpl, playerId: UUID): JsonElement? {
    return loggedTransaction {
        stat.statMapId?.let { statMapId ->
            StatEntries.select(selectStatEntries(statMapId, playerId)).singleOrNull()?.let { row ->
                row[StatEntries.statValue].bytes.fromCbor()
            }
        }
    }
}

fun databaseSet(stat: StatisticImpl, playerId: UUID, value: JsonElement) {
    loggedTransaction {
        // Get a pool ID, or make a new one
        val statPoolId = stat.statPoolId ?: StatPools.insert {
            it[pluginName] = stat.pluginName
            it[statName] = stat.identifier
        }[StatPools.statPoolId].also { stat.statPoolId = it }
        // Get a map ID, or make a new one
        val statMapId = stat.statMapId ?: StatMaps.insert {
            it[this.statPoolId] = statPoolId
            it[serverName] = stat.serverName
        }[StatMaps.statMapId].also { stat.statMapId = it }
        // Update or create an entry for the value
        val statValue = ExposedBlob(value.toCbor())
        val updateResult = StatEntries.update({ selectStatEntries(statMapId, playerId )}) {
            it[this.statValue] = statValue
        }
        if (updateResult == 0) {
            StatEntries.insert {
                it[this.statMapId] = statMapId
                it[this.playerId] = playerId
                it[this.statValue] = statValue
            }
        }
    }
}

fun databaseRemove(stat: StatisticImpl, playerId: UUID): Boolean {
    return loggedTransaction {
        stat.statMapId?.let { statMapId ->
            if (StatEntries.remove(statMapId, playerId)) {
                // If that was the last entry in this map, delete the map
                if (!StatEntries.containsPool(statMapId)) {
                    stat.statMapId = null
                    StatMaps.delete(statMapId)
                    stat.statPoolId?.let { statPoolId ->
                        // If that was the last map in this pool, delete the pool
                        if (!StatMaps.containsPool(statPoolId)) {
                            stat.statPoolId = null
                            StatPools.delete(statPoolId)
                        }
                    }
                }
                true
            } else {
                false
            }
        } ?: false
    }
}

fun databaseReset(stat: StatisticImpl) {
    loggedTransaction {
        stat.statMapId?.let { statMapId ->
            if (StatEntries.reset(statMapId)) {
                stat.statMapId = null
                StatMaps.delete(statMapId)
                stat.statPoolId?.let { statPoolId ->
                    // If that was the last map in this pool, delete the pool
                    if (!StatMaps.containsPool(statPoolId)) {
                        stat.statPoolId = null
                        StatPools.delete(statPoolId)
                    }
                }
            }
        }
    }
}