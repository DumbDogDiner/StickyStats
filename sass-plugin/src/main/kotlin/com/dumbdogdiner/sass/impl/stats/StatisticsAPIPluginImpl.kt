package com.dumbdogdiner.sass.impl.stats

import com.dumbdogdiner.sass.api.stats.StatisticsAPIPlugin
import com.dumbdogdiner.sass.impl.stats.store.StoreImpl
import org.bukkit.plugin.java.JavaPlugin

object StatisticsAPIPluginImpl : StatisticsAPIPlugin {
    private val storeMap = mutableMapOf<Pair<JavaPlugin, String>, StoreImpl>()

    override fun getStore(plugin: JavaPlugin, serverName: String) =
        storeMap.getOrPut(plugin to serverName) { StoreImpl(plugin, serverName) }
}