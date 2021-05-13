package com.dumbdogdiner.sass

import com.dumbdogdiner.sass.command.ChallengesCommand
import com.dumbdogdiner.sass.impl.reward.RewardsAPIPluginImpl
import com.dumbdogdiner.sass.impl.stats.StatisticsAPIPluginImpl
import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIConfig
import kr.entree.spigradle.annotations.PluginMain
import net.milkbowl.vault.economy.Economy
import org.bukkit.Bukkit
import org.bukkit.plugin.ServicePriority
import org.bukkit.plugin.java.JavaPlugin

@PluginMain
class SassPlugin : JavaPlugin() {
    override fun onLoad() {
        CommandAPI.onLoad(CommandAPIConfig())

        val servicesManager = Bukkit.getServicesManager()

        servicesManager.register(com.dumbdogdiner.sass.api.stats.StatisticsAPIPlugin::class.java, StatisticsAPIPluginImpl, this, ServicePriority.Lowest)
        servicesManager.register(com.dumbdogdiner.sass.api.reward.RewardsAPIPlugin::class.java, RewardsAPIPluginImpl, this, ServicePriority.Lowest)
    }

    override fun onEnable() {
        CommandAPI.onEnable(this)
        CommandAPI.registerCommand(ChallengesCommand::class.java)

        // we need to find an econ provider in order to deal out rewards
        RewardsAPIPluginImpl.economy = server.servicesManager.getRegistration(Economy::class.java)?.provider
            ?: throw IllegalStateException("An economy provider is required for this plugin!")
    }
}