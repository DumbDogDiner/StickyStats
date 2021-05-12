package com.dumbdogdiner.sass.stats

import com.dumbdogdiner.sass.stats.api.StatisticsAPIPlugin
import com.dumbdogdiner.sass.stats.impl.StatisticsAPIPluginImpl
import kr.entree.spigradle.annotations.PluginMain
import org.bukkit.Bukkit
import org.bukkit.plugin.ServicePriority
import org.bukkit.plugin.java.JavaPlugin

@PluginMain
class SassStatsPlugin : JavaPlugin() {
    override fun onLoad() {
        Bukkit
            .getServicesManager()
            .register(StatisticsAPIPlugin::class.java, StatisticsAPIPluginImpl, this, ServicePriority.Lowest)
    }
}
