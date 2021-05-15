package com.dumbdogdiner.sass.db

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.select
import java.util.UUID

object StatEntries : Table("sass_stat_entries") {
    val statMapId = integer("stat_map_id")
    val playerId = uuid("player_id")
    val statValue = blob("stat_value")

    override val primaryKey = PrimaryKey(statMapId, playerId)

    fun remove(id: Int, playerId: UUID) =
        deleteWhere { statMapId eq id and (StatEntries.playerId eq playerId) } != 0

    fun reset(id: Int) = deleteWhere { statMapId eq id } != 0

    fun containsPool(id: Int) = select { statMapId eq id }.limit(1).any()
}