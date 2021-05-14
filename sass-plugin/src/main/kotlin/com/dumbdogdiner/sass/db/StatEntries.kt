package com.dumbdogdiner.sass.db

import org.jetbrains.exposed.sql.Table

object StatEntries : Table("sass_stat_entries") {
    val statMapIdCol = integer("stat_map_id")
    val playerIdCol = uuid("player_id")
    val statValueCol = blob("stat_value")

    override val primaryKey = PrimaryKey(statMapIdCol, playerIdCol)
}