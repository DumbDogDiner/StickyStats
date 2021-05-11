package com.dumbdogdiner.sass.stats.impl.store.statistic

import com.dumbdogdiner.sass.api.store.statistic.Statistic
import com.dumbdogdiner.sass.stats.impl.event.StatisticsEventImpl
import com.dumbdogdiner.sass.stats.impl.store.StoreImpl
import com.google.gson.JsonElement
import java.util.UUID

class StatisticImpl(
    private val identifier: String,
    private val store: StoreImpl,
) : Statistic {
    private val valueMap = mutableMapOf<UUID, JsonElement>()
    private var invalid = false
    private var event = null as StatisticsEventImpl?

    override fun getIdentifier(): String {
        ensureValid()
        return identifier
    }

    override fun getStore(): StoreImpl {
        ensureValid()
        return store
    }

    override fun getEvent() = event ?: run {
        val result = StatisticsEventImpl(this)
        event = result
        result
    }

    override fun delete() {
        ensureValid()
        store.delete(identifier)
        invalid = true
    }

    override fun get(playerId: UUID): JsonElement? {
        ensureValid()
        return valueMap[playerId]
    }

    override fun set(playerId: UUID, value: JsonElement) {
        ensureValid()
        valueMap[playerId] = value
        event?.handlers?.forEach { it.execute(this, playerId) }
    }

    override fun remove(playerId: UUID): Boolean {
        ensureValid()
        return valueMap.remove(playerId) != null
    }

    private fun ensureValid() {
        if (invalid) throw IllegalStateException("Use of invalid Statistic '$identifier'")
    }

    fun deleteEvent() {
        event = null
    }
}