package com.dumbdogdiner.sass.db

import org.jetbrains.exposed.sql.Table

object StatPools : Table("sass_stat_pools") {
    val pluginNameCol = text("plugin_name")
    val statNameCol = text("stat_name")
    val statPoolIdCol = integer("stat_pool_id").autoIncrement()

    override val primaryKey = PrimaryKey(statPoolIdCol)
}