package com.dumbdogdiner.sass.stats.impl.store.statistic

import com.dumbdogdiner.sass.api.store.statistic.Statistic
import com.dumbdogdiner.sass.stats.impl.store.StoreImpl
import com.google.gson.JsonElement
import java.util.UUID

class StatisticImpl(
    private val identifier: String,
    private val store: StoreImpl,
) : Statistic {
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
    }

    override fun remove(playerId: UUID): Boolean {
        ensureValid()
        return valueMap.remove(playerId) != null
    }

    private fun ensureValid() {
        if (invalid) throw IllegalStateException("Use of invalid Statistic '$identifier'")
    }
}