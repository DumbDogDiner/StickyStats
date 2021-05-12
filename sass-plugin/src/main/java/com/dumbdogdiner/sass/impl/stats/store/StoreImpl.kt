package com.dumbdogdiner.sass.impl.stats.store

import com.dumbdogdiner.sass.api.stats.store.Store
import com.dumbdogdiner.sass.impl.stats.store.statistic.StatisticImpl
import org.bukkit.plugin.java.JavaPlugin

class StoreImpl(
    private val plugin: JavaPlugin,
    private val serverName: String,
) : Store {
    private val statMap = mutableMapOf<String, StatisticImpl>()

    override fun getPlugin() = plugin

    override fun getServerName() = serverName

    override fun get(id: String) = statMap[id]

    override fun create(id: String) = id !in statMap && run {
        statMap[id] = StatisticImpl(id, this)
        true
    }

    fun delete(id: String) {
        statMap -= id
    }
}