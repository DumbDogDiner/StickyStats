package com.dumbdogdiner.sass.db

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.select

/**
 * Table to group pools and servers into "maps".
 */
object StatMaps : Table("sass_stat_maps") {
    /** The ID of the stat pool. */
    val statPoolId = integer("stat_pool_id")
    /** The name of the server, or null if this statistic is global. */
    val serverName = text("server_name").nullable()
    /** A generated ID number representing the previous two values. */
    val statMapId = integer("stat_map_id").autoIncrement()

    override val primaryKey = PrimaryKey(statMapId)

    /**
     * Delete the given map. The caller should check to ensure this map is not referenced.
     */
    fun delete(id: Int) = deleteWhere { statMapId eq id }

    /**
     * Check if any maps reference a given pool.
     */
    fun containsPool(id: Int) = select { statPoolId eq id }.limit(1).any()
}