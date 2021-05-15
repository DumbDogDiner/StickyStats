package com.dumbdogdiner.sass.db

import com.dumbdogdiner.sass.impl.stats.StatisticImpl
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.select

/**
 * Table to group pools and servers into "maps".
 */
internal object StatMaps : Table("sass_stat_maps") {
    /** The ID of the stat pool. */
    val statPoolId = integer("stat_pool_id")
    /** The name of the server, or null if this statistic is global. */
    val serverName = text("server_name").nullable()
    /** A generated ID number representing the previous two values. */
    val statMapId = integer("stat_map_id").autoIncrement()

    override val primaryKey = PrimaryKey(statMapId)

    /**
     * Remove [stat] from this table, and possibly modify [StatPools] if a pool is no longer referenced.
     */
    fun delete(stat: StatisticImpl) {
        stat.statMapId?.let { statMapId ->
            stat.statMapId = null
            deleteWhere { StatMaps.statMapId eq statMapId }
            stat.statPoolId?.let { statPoolId ->
                if (select { StatMaps.statPoolId eq statPoolId }.limit(1).none()) {
                    stat.statPoolId = null
                    StatPools.deleteWhere { StatPools.statPoolId eq statPoolId }
                }
            }
        }
    }
}