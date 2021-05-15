package com.dumbdogdiner.sass.db

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.deleteWhere

/**
 * Table to group plugins and stat names into "pools".
 */
object StatPools : Table("sass_stat_pools") {
    /** The name of the plugin. */
    val pluginName = text("plugin_name")
    /** The name of the statistic. */
    val statName = text("stat_name")
    /** A generated ID number representing the previous two values. */
    val statPoolId = integer("stat_pool_id").autoIncrement()

    override val primaryKey = PrimaryKey(statPoolId)

    /**
     * Delete the given pool. The caller should check to ensure this pool is not referenced.
     */
    fun delete(id: Int) = deleteWhere { statPoolId eq id }
}