package com.dumbdogdiner.sass.db

import org.jetbrains.exposed.sql.Table

object StatMaps : Table("sass_stat_maps") {
    val statPoolId = integer("stat_pool_id")
    val serverName = text("server_name").nullable()
    val statMapId = integer("stat_map_id").autoIncrement()

    override val primaryKey = PrimaryKey(statMapId)
}