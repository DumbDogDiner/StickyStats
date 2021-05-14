package com.dumbdogdiner.sass.db

import org.jetbrains.exposed.sql.Table

object StatMaps : Table("sass_stat_maps") {
    val statPoolIdCol = integer("stat_pool_id")
    val serverNameCol = text("server_name").nullable()
    val statMapIdCol = integer("stat_map_id").autoIncrement()

    override val primaryKey = PrimaryKey(statMapIdCol)
}