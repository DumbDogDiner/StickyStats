package com.dumbdogdiner.sass.impl.stats

import com.dumbdogdiner.sass.api.event.StatisticModifiedEvent
import com.dumbdogdiner.sass.api.stats.Statistic
import com.dumbdogdiner.sass.db.SassStatistics
import com.google.common.collect.MapMaker
import com.google.gson.JsonElement
import org.bukkit.Bukkit
import java.util.UUID

class StatisticImpl(
    private val identifier: String,
    private val store: StoreImpl,
) : Statistic {
    private val valueMap = MapMaker().weakValues().makeMap<UUID, CachedElement>()

    override fun getIdentifier() = identifier

    override fun getStore() = store

    override fun reset() {
        SassStatistics.reset(this)
    }

    override fun get(playerId: UUID) = (valueMap[playerId] ?: run {
        val result = CachedElement(SassStatistics.get(this, playerId))
        valueMap[playerId] = result
        result
    }).element

    override fun set(playerId: UUID, value: JsonElement) {
        val oldValue = this[playerId]
        valueMap[playerId] = CachedElement(value)
        SassStatistics.put(this, playerId, value)
        val event = StatisticModifiedEvent(this, playerId, oldValue, value)
        Bukkit.getPluginManager().callEvent(event)
        if (event.isCancelled) {
            if (oldValue == null) {
                valueMap -= playerId
                SassStatistics.delete(this, playerId)
            } else {
                valueMap[playerId] = CachedElement(oldValue)
                SassStatistics.put(this, playerId, oldValue)
            }
        }
    }

    override fun remove(playerId: UUID): Boolean {
        valueMap.remove(playerId)
        return SassStatistics.delete(this, playerId) != 0
    }

    private class CachedElement(val element: JsonElement?)
}