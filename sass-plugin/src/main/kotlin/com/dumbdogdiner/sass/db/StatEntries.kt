package com.dumbdogdiner.sass.db

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.select
import java.util.UUID

/**
 * Table to map stat maps and player IDs to CBOR-encoded values.
 */
object StatEntries : Table("sass_stat_entries") {
    /** The ID of the stat map. */
    val statMapId = integer("stat_map_id")
    /** The ID of the player. */
    val playerId = uuid("player_id")
    /** The CBOR-encoded value. */
    val statValue = blob("stat_value")

    override val primaryKey = PrimaryKey(statMapId, playerId)

    /**
     * Delete an entry with the given map ID and player ID, returning true if something was deleted.
     */
    fun remove(id: Int, playerId: UUID) =
        deleteWhere { statMapId eq id and (StatEntries.playerId eq playerId) } != 0

    /**
     * Delete all values for a given stat map ID.
     */
    fun reset(id: Int) = deleteWhere { statMapId eq id } != 0

    /**
     * Check if any entries reference a given map.
     */
    fun containsMap(id: Int) = select { statMapId eq id }.limit(1).any()
}