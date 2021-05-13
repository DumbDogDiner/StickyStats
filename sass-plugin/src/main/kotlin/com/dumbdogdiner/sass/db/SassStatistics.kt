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

object SassStatistics : Table() {
    private val identifierColumn = text("identifier")
    private val pluginColumn = text("plugin")
    private val serverColumn = text("server")
    private val playerIdColumn = uuid("player_id")
    private val dataColumn = blob("data")

    private lateinit var db: Database

    private fun selector(stat: StatisticImpl) = (identifierColumn eq stat.identifier) and
            (pluginColumn eq stat.store.plugin.name) and
            (serverColumn eq stat.store.serverName)

    private fun selector(stat: StatisticImpl, playerId: UUID) = selector(stat) and (playerIdColumn eq playerId)

    fun put(stat: StatisticImpl, id: UUID, data: JsonElement) = transaction(db) {
        val updateResult = update({ selector(stat, id) }) {
            it[dataColumn] = ExposedBlob(data.toCbor())
        }
        if (updateResult == 0) insert {
            it[identifierColumn] = stat.identifier
            it[pluginColumn] = stat.store.plugin.name
            it[serverColumn] = stat.store.serverName
            it[playerIdColumn] = id
            it[dataColumn] = ExposedBlob(data.toCbor())
        }
    }

    fun get(stat: StatisticImpl, id: UUID) = transaction(db) {
        select(selector(stat, id)).firstOrNull()?.let {
            it[dataColumn].bytes.fromCbor()
        }
    }

    fun delete(stat: StatisticImpl, id: UUID) = transaction(db) {
        deleteWhere { selector(stat, id) }
    }

    fun reset(stat: StatisticImpl) = transaction(db) {
        deleteWhere { selector(stat) }
    }

    fun init(db: Database) {
        this.db = db

        transaction(db) {
            addLogger(object : SqlLogger {
                override fun log(context: StatementContext, transaction: Transaction) {
                    SassPlugin.instance.logger.info("[SQL] ${context.expandArgs(transaction)}")
                }
            })
            if (!exists()) {
                SchemaUtils.create(SassStatistics)
            }
        }
    }
}