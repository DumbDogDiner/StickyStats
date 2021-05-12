package com.dumbdogdiner.sass.db

import org.jetbrains.exposed.sql.Table

object SassStatistics : Table() {
    val identifier = text("identifier")
    val plugin = text("plugin")
    val server = text("server")
    val playerId = uuid("player_id")
    val data = blob("data")
}