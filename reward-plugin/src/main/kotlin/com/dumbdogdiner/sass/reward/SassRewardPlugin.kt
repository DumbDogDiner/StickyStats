package com.dumbdogdiner.sass.reward

import com.dumbdogdiner.sass.reward.api.RewardsAPIPlugin
import com.dumbdogdiner.sass.reward.command.ChallengesCommand
import com.dumbdogdiner.sass.reward.impl.RewardsAPIPluginImpl
import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIConfig
import kr.entree.spigradle.annotations.PluginMain
import org.bukkit.Bukkit
import org.bukkit.plugin.ServicePriority
import org.bukkit.plugin.java.JavaPlugin

@PluginMain
class SassRewardPlugin : JavaPlugin() {
    override fun onLoad() {
        CommandAPI.onLoad(CommandAPIConfig())

        Bukkit
            .getServicesManager()
            .register(RewardsAPIPlugin::class.java, RewardsAPIPluginImpl, this, ServicePriority.Lowest)
    }

    override fun onEnable() {
        CommandAPI.onEnable(this)
        CommandAPI.registerCommand(ChallengesCommand::class.java)
    }
}
