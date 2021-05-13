package com.dumbdogdiner.sass.impl.stats.store.statistic

import com.dumbdogdiner.sass.impl.stats.store.StoreImpl
import com.google.gson.JsonElement
import org.bukkit.Bukkit
import java.util.UUID

class StatisticImpl(
    private val identifier: String,
    private val store: StoreImpl,
) : com.dumbdogdiner.sass.api.stats.store.statistic.Statistic {
    private val valueMap = mutableMapOf<UUID, JsonElement>()
    private var invalid = false

    override fun getIdentifier(): String {
        ensureValid()
        return identifier
    }

    override fun getStore(): StoreImpl {
        ensureValid()
        return store
    }

    override fun delete() {
        ensureValid()
        store.remove(identifier)
        invalid = true
    }

    override fun get(playerId: UUID): JsonElement? {
        ensureValid()
        return valueMap[playerId]
    }

    override fun set(playerId: UUID, value: JsonElement) {
        ensureValid()
        val oldValue = valueMap[playerId]
        valueMap[playerId] = value
        val event = com.dumbdogdiner.sass.api.event.StatisticModifiedEvent(this, playerId, oldValue, value)
        Bukkit.getPluginManager().callEvent(event)
        if (event.isCancelled) {
            if (oldValue == null) {
                valueMap -= playerId
            } else {
                valueMap[playerId] = oldValue
            }
        }
    }

    override fun remove(playerId: UUID): Boolean {
        ensureValid()
        return valueMap.remove(playerId) != null
    }

    private fun ensureValid() {
        if (invalid) throw IllegalStateException("Use of invalid Statistic '$identifier'")
    }
}