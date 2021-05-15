package com.dumbdogdiner.sass.db

import com.dumbdogdiner.sass.impl.stats.StatisticImpl
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import kotlin.reflect.KProperty

private class CachedValue(val value: Int?)

class CachedStatPool {
    private var cachedValue = null as CachedValue?

    operator fun getValue(stat: StatisticImpl, property: KProperty<*>): Int? {
        return (this.cachedValue ?: loggedTransaction {
            val fromDb = StatPools
                .select { StatPools.pluginName eq stat.pluginName and (StatPools.statName eq stat.identifier) }
                .singleOrNull()
                ?.get(StatPools.statPoolId)
            CachedValue(fromDb).also { cachedValue = it }
        }).value
    }

    fun invalidateCache() {
        cachedValue = null
    }

    fun overrideCache(value: Int) {
        cachedValue = CachedValue(value)
    }
}

class CachedStatMap {
    private var cachedValue = null as CachedValue?

    operator fun getValue(stat: StatisticImpl, property: KProperty<*>): Int? {
        return (this.cachedValue ?: loggedTransaction {
            val statPool = stat.statPoolId ?: return@loggedTransaction CachedValue(null)
            val fromDb = StatMaps
                .select { StatMaps.statPoolId eq statPool and (StatMaps.serverName eq stat.serverName) }
                .singleOrNull()
                ?.get(StatMaps.statMapId)
            CachedValue(fromDb).also { cachedValue = it }
        }).value
    }

    fun invalidateCache() {
        cachedValue = null
    }

    fun overrideCache(value: Int) {
        cachedValue = CachedValue(value)
    }
}