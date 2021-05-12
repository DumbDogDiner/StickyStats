package com.dumbdogdiner.sass.stats.impl.store

import com.dumbdogdiner.sass.stats.api.store.Store
import com.dumbdogdiner.sass.stats.impl.store.statistic.StatisticImpl
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