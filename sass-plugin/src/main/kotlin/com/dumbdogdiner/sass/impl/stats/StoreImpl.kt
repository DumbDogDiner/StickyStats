package com.dumbdogdiner.sass.impl.stats

import com.dumbdogdiner.sass.api.stats.Store
import com.google.common.collect.MapMaker
import org.bukkit.plugin.java.JavaPlugin

class StoreImpl(private val plugin: JavaPlugin, private val global: Boolean) : Store {
    private val statMap = MapMaker().weakValues().makeMap<String, StatisticImpl>()

    override fun getPlugin() = plugin

    override fun isGlobal() = global

    override fun get(id: String): StatisticImpl =
        statMap.getOrPut(id) { StatisticImpl(id, this) }
}
