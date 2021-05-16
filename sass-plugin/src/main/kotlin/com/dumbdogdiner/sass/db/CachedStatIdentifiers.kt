package com.dumbdogdiner.sass.db

import com.dumbdogdiner.sass.impl.stats.StatisticImpl
import com.dumbdogdiner.sass.util.CachedNullable
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import kotlin.reflect.KProperty

/**
 * Handles reading and caching of the stat pool ID for a statistic.
 */
class CachedStatPool {
    private var cachedValue = null as CachedNullable<Int>?

    operator fun getValue(stat: StatisticImpl, property: KProperty<*>): Int? {
        val v = this.cachedValue ?: loggedTransaction {
            val fromDb = StatPools
                .select { StatPools.pluginName eq stat.pluginName and (StatPools.statName eq stat.identifier) }
                .singleOrNull()
                ?.get(StatPools.statPoolId)
            CachedNullable(fromDb).also { cachedValue = it }
        }
        return v.value
    }

    operator fun setValue(stat: StatisticImpl, property: KProperty<*>, value: Int?) {
        // override the cache
        cachedValue = value?.let(::CachedNullable)
    }
}

/**
 * Handles reading and caching of the stat map ID for a statistic.
 */
class CachedStatMap {
    private var cachedValue = null as CachedNullable<Int>?

    operator fun getValue(stat: StatisticImpl, property: KProperty<*>): Int? {
        val v = this.cachedValue ?: loggedTransaction {
            stat.statPoolId?.let { statPoolId ->
                val fromDb = StatMaps
                    .select { StatMaps.statPoolId eq statPoolId and (StatMaps.serverName eq stat.serverName) }
                    .singleOrNull()
                    ?.get(StatMaps.statMapId)
                CachedNullable(fromDb).also { cachedValue = it }
            } ?: CachedNullable(null)
        }
        return v.value
    }

    operator fun setValue(stat: StatisticImpl, property: KProperty<*>, value: Int?) {
        // override the cache
        cachedValue = value?.let(::CachedNullable)
    }
}
