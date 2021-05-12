package com.dumbdogdiner.sass.stats.impl.store.statistic

import com.dumbdogdiner.sass.api.store.statistic.Statistic
import com.dumbdogdiner.sass.stats.impl.event.StatisticEventContextImpl
import com.dumbdogdiner.sass.stats.impl.event.StatisticEventImpl
import com.dumbdogdiner.sass.stats.impl.store.StoreImpl
import com.google.gson.JsonElement
import java.util.UUID

class StatisticImpl(
    private val identifier: String,
    private val store: StoreImpl,
) : Statistic {
    private val valueMap = mutableMapOf<UUID, JsonElement>()
    private var invalid = false
    private var event = null as StatisticEventImpl?

    override fun getIdentifier(): String {
        ensureValid()
        return identifier
    }

    override fun getStore(): StoreImpl {
        ensureValid()
        return store
    }

    override fun getEvent() = event ?: run {
        val result = StatisticEventImpl(this)
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
        val oldValue = valueMap[playerId]
        val ctx = StatisticEventContextImpl(this, playerId, oldValue, value)
        event?.handlers?.let {
            for (handler in it) {
                if (ctx.isCanceled) break
                handler.execute(ctx)
            }
        }
        if (!ctx.isCanceled) {
            valueMap[playerId] = value
        }
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