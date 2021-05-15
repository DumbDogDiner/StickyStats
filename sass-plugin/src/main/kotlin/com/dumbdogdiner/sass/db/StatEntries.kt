package com.dumbdogdiner.sass.db

import org.jetbrains.exposed.sql.Table

object StatEntries : Table("sass_stat_entries") {
    val statMapId = integer("stat_map_id")
    val playerId = uuid("player_id")
    val statValue = blob("stat_value")

    override val primaryKey = PrimaryKey(statMapId, playerId)
}