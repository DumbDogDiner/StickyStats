package com.dumbdogdiner.sass.impl.stats

import com.dumbdogdiner.sass.api.event.StatisticModifiedEvent
import com.dumbdogdiner.sass.api.stats.Statistic
import com.dumbdogdiner.sass.db.databaseGet
import com.dumbdogdiner.sass.db.databaseRemove
import com.dumbdogdiner.sass.db.databaseReset
import com.dumbdogdiner.sass.db.databaseSet
import com.google.common.collect.MapMaker
import com.google.gson.JsonElement
import org.bukkit.Bukkit
import java.util.UUID

class StatisticImpl(
    private val identifier: String,
    private val store: StoreImpl,
) : Statistic {
    private val valueMap = MapMaker().weakValues().makeMap<UUID, CachedElement>()
    var cachedStatPool = null as Int?
    var cachedStatMap = null as Int?

    override fun getIdentifier() = identifier

    override fun getStore() = store

    override fun reset() {
        databaseReset(this)
    }

    override fun get(playerId: UUID) = (valueMap[playerId] ?: run {
        val result = CachedElement(databaseGet(this, playerId))
        valueMap[playerId] = result
        result
    }).element

    override fun set(playerId: UUID, value: JsonElement) {
        val oldValue = this[playerId]
        valueMap[playerId] = CachedElement(value)
        databaseSet(this, playerId, value)
        val event = StatisticModifiedEvent(this, playerId, oldValue, value)
        Bukkit.getPluginManager().callEvent(event)
        if (event.isCancelled) {
            if (oldValue == null) {
                valueMap -= playerId
                databaseRemove(this, playerId)
            } else {
                valueMap[playerId] = CachedElement(oldValue)
                databaseSet(this, playerId, oldValue)
            }
        }
    }

    override fun remove(playerId: UUID): Boolean {
        valueMap.remove(playerId)
        return databaseRemove(this, playerId)
    }

    private class CachedElement(val element: JsonElement?)
}