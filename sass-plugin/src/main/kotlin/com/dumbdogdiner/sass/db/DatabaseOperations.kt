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
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.statements.StatementContext
import org.jetbrains.exposed.sql.statements.api.ExposedBlob
import org.jetbrains.exposed.sql.statements.expandArgs
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.util.UUID

/** The database to get information from. */
private lateinit var db: Database
/** The logger to log SQL queries. */
private lateinit var logger: SqlLogger

/**
 * Initialize these database methods.
 */
fun databaseInit(database: Database) {
    db = database
    logger = object : SqlLogger {
        override fun log(context: StatementContext, transaction: Transaction) {
            SassPlugin.instance.logger.info("[SQL] ${context.expandArgs(transaction)}")
        }
    }

    // make sure the needed tables exist
    loggedTransaction {
        SchemaUtils.create(StatPools)
        SchemaUtils.create(StatMaps)
        SchemaUtils.create(StatEntries)
    }
}

/**
 * Perform a transaction using the correct database and logger.
 */
internal fun <T> loggedTransaction(statement: Transaction.() -> T) = transaction(db) {
    addLogger(logger)
    statement()
}

/**
 * Return a selector to find the stat entry with the given map ID and player ID.
 */
private fun selectStatEntries(statMapId: Int, playerId: UUID) =
    StatEntries.statMapId eq statMapId and (StatEntries.playerId eq playerId)

/**
 * Get the value of [stat] for [playerId] directly from the database.
 */
fun databaseGet(stat: StatisticImpl, playerId: UUID): JsonElement? {
    return loggedTransaction {
        stat.statMapId?.let { statMapId ->
            StatEntries.select(selectStatEntries(statMapId, playerId)).singleOrNull()?.let { row ->
                row[StatEntries.statValue].bytes.fromCbor()
            }
        }
    }
}

/**
 * Set the value of [stat] for [playerId] to [value], creating rows as needed.
 */
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
        val updateResult = StatEntries.update({ selectStatEntries(statMapId, playerId) }) {
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

/**
 * Remove the value for [playerId] in [stat], additionally removing unreferenced rows.
 */
fun databaseRemove(stat: StatisticImpl, playerId: UUID): Boolean {
    return loggedTransaction {
        StatEntries.remove(stat, playerId)
    }
}

/**
 * Remove [stat] and all its values, additionally removing unreferenced rows.
 */
fun databaseReset(stat: StatisticImpl) {
    loggedTransaction {
        StatEntries.reset(stat)
    }
}
