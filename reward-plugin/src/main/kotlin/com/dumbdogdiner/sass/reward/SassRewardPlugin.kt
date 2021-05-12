package com.dumbdogdiner.sass.reward

import com.dumbdogdiner.sass.reward.api.RewardsAPIPlugin
import com.dumbdogdiner.sass.reward.impl.RewardsAPIPluginImpl
import kr.entree.spigradle.annotations.PluginMain
import org.bukkit.Bukkit
import org.bukkit.plugin.ServicePriority
import org.bukkit.plugin.java.JavaPlugin

@PluginMain
class SassRewardPlugin : JavaPlugin() {
    override fun onLoad() {
        Bukkit
            .getServicesManager()
            .register(RewardsAPIPlugin::class.java, RewardsAPIPluginImpl, this, ServicePriority.Lowest)
    }
}
