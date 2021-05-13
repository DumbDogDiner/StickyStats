package com.dumbdogdiner.sass.impl.stats.store

import com.dumbdogdiner.sass.api.stats.store.Store
import com.dumbdogdiner.sass.impl.stats.store.statistic.StatisticImpl
import com.google.common.collect.MapMaker
import org.bukkit.plugin.java.JavaPlugin

class StoreImpl(
    private val plugin: JavaPlugin,
    private val serverName: String,
) : Store {
    private val statMap = MapMaker().weakValues().makeMap<String, StatisticImpl>()

    override fun getPlugin() = plugin

    override fun getServerName() = serverName

    override fun get(id: String): StatisticImpl =
        statMap.getOrPut(id) { StatisticImpl(id, this) }
}