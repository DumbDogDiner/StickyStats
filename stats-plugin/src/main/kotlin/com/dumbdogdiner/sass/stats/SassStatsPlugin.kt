package com.dumbdogdiner.sass.stats

import com.dumbdogdiner.sass.api.StatisticsAPIPlugin
import com.dumbdogdiner.sass.stats.impl.StatisticsAPIPluginImpl
import kr.entree.spigradle.annotations.PluginMain
import org.bukkit.plugin.java.JavaPlugin

@PluginMain
class SassStatsPlugin : JavaPlugin() {
    override fun onLoad() {
        StatisticsAPIPlugin.register(this, StatisticsAPIPluginImpl())
    }
}
