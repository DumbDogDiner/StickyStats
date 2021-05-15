package com.dumbdogdiner.sass.db

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.select

object StatMaps : Table("sass_stat_maps") {
    val statPoolId = integer("stat_pool_id")
    val serverName = text("server_name").nullable()
    val statMapId = integer("stat_map_id").autoIncrement()

    override val primaryKey = PrimaryKey(statMapId)

    fun delete(id: Int) = deleteWhere { statMapId eq id }

    fun containsPool(id: Int) = select { statPoolId eq id }.limit(1).any()
}