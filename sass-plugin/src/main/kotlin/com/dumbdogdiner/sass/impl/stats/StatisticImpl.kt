package com.dumbdogdiner.sass.impl.stats

import com.dumbdogdiner.sass.SassPlugin
import com.dumbdogdiner.sass.api.event.StatisticModifiedEvent
import com.dumbdogdiner.sass.api.event.StatisticRemovedEvent
import com.dumbdogdiner.sass.api.event.StatisticResetEvent
import com.dumbdogdiner.sass.api.stats.Statistic
import com.dumbdogdiner.sass.db.CachedStatMap
import com.dumbdogdiner.sass.db.CachedStatPool
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

    val statPoolIdDelegate = CachedStatPool()
    val statMapIdDelegate = CachedStatMap()

    val statPoolId by this.statPoolIdDelegate
    val statMapId by this.statMapIdDelegate

    val pluginName get() = this.store.plugin.name
    val serverName get() = if (this.store.isGlobal) SassPlugin.instance.serverName else null

    override fun getIdentifier() = identifier

    override fun getStore() = store

    override fun reset() {
        databaseReset(this)
        Bukkit.getPluginManager().callEvent(StatisticResetEvent(this))
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
        val oldValue = this[playerId]
        valueMap.remove(playerId)
        return if (databaseRemove(this, playerId)) {
            Bukkit.getPluginManager().callEvent(StatisticRemovedEvent(this, playerId, oldValue!!))
            true
        } else {
            false
        }
    }

    private class CachedElement(val element: JsonElement?)
}