package com.dumbdogdiner.sass.stats.impl

import com.dumbdogdiner.sass.stats.api.StatisticsAPIPlugin
import com.dumbdogdiner.sass.stats.impl.store.StoreImpl
import org.bukkit.plugin.java.JavaPlugin

class StatisticsAPIPluginImpl : StatisticsAPIPlugin {
    private val storeMap = mutableMapOf<Pair<JavaPlugin, String>, StoreImpl>()

    override fun getStore(plugin: JavaPlugin, serverName: String) =
        storeMap.getOrPut(plugin to serverName) { StoreImpl(plugin, serverName) }
}