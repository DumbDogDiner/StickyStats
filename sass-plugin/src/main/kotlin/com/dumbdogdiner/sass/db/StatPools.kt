package com.dumbdogdiner.sass.db

import org.jetbrains.exposed.sql.Table

object StatPools : Table("sass_stat_pools") {
    val pluginName = text("plugin_name")
    val statName = text("stat_name")
    val statPoolId = integer("stat_pool_id").autoIncrement()

    override val primaryKey = PrimaryKey(statPoolId)
}