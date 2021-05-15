package com.dumbdogdiner.sass.db

import com.dumbdogdiner.sass.impl.stats.StatisticImpl
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.select
import java.util.UUID

/**
 * Table to map stat maps and player IDs to CBOR-encoded values.
 */
internal object StatEntries : Table("sass_stat_entries") {
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
    fun remove(stat: StatisticImpl, playerId: UUID): Boolean {
        stat.statMapId?.let { statMapId ->
            if (deleteWhere { StatEntries.statMapId eq statMapId and (StatEntries.playerId eq playerId) } != 0) {
                // If that was the last entry in this map, delete the map
                if (select { StatEntries.statMapId eq statMapId }.limit(1).none()) {
                    StatMaps.delete(stat)
                }
                return true
            }
        }
        return false
    }

    /**
     * Delete all values for a given stat map ID, additionally removing unused rows.
     */
    fun reset(stat: StatisticImpl) {
        stat.statMapId?.let { statMapId ->
            if (deleteWhere { StatEntries.statMapId eq statMapId } != 0) {
                StatMaps.delete(stat)
            }
        }
    }
}