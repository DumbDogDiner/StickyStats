package com.dumbdogdiner.sass.impl.stats.store

import com.dumbdogdiner.sass.impl.stats.store.statistic.StatisticImpl
import org.bukkit.plugin.java.JavaPlugin

class StoreImpl(
    private val plugin: JavaPlugin,
    private val serverName: String,
) : HashMap<String, StatisticImpl>(), com.dumbdogdiner.sass.api.stats.store.Store {


    override fun getPlugin() = plugin

    override fun getServerName() = serverName

    override fun create(id: String) = id !in this && run {
        this[id] = StatisticImpl(id, this)
        true
    }
}