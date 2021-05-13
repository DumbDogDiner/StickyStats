package com.dumbdogdiner.sass

import com.dumbdogdiner.sass.api.reward.RewardsAPIPlugin
import com.dumbdogdiner.sass.api.stats.StatisticsAPIPlugin
import com.dumbdogdiner.sass.command.ChallengesCommand
import com.dumbdogdiner.sass.db.SassStatistics
import com.dumbdogdiner.sass.impl.reward.RewardsAPIPluginImpl
import com.dumbdogdiner.sass.impl.stats.StatisticsAPIPluginImpl
import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIConfig
import kr.entree.spigradle.annotations.PluginMain
import net.milkbowl.vault.economy.Economy
import org.bukkit.Bukkit
import org.bukkit.plugin.ServicePriority
import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.exposed.sql.Database
import java.net.URI

@PluginMain
class SassPlugin : JavaPlugin() {
    override fun onLoad() {
        CommandAPI.onLoad(CommandAPIConfig())

        val servicesManager = Bukkit.getServicesManager()

        servicesManager.register(StatisticsAPIPlugin::class.java, StatisticsAPIPluginImpl, this, ServicePriority.Lowest)
        servicesManager.register(RewardsAPIPlugin::class.java, RewardsAPIPluginImpl, this, ServicePriority.Lowest)
    }

    override fun onEnable() {
        CommandAPI.onEnable(this)
        CommandAPI.registerCommand(ChallengesCommand::class.java)

        // we need to find an econ provider in order to deal out rewards
        RewardsAPIPluginImpl.economy = server.servicesManager.getRegistration(Economy::class.java)?.provider
            ?: throw IllegalStateException("An economy provider is required for this plugin!")

        // read this plugin's config to get the database
        val db = config.let {
            val database = it.getString("db.database") ?: throw IllegalStateException("Missing database")
            val host = it.getString("db.host") ?: throw IllegalStateException("Missing host")
            val port = it.getInt("db.port", 5432)
            val username = it.getString("db.username") ?: throw IllegalStateException("Missing username")
            val password = it.getString("db.password") ?: throw IllegalStateException("Missing password")

            Database.connect(
                url = URI("jdbc:postgresql", null, host, port, "/$database", null, null).toString(),
                user = username,
                password = password,
            )
        }

        SassStatistics.init(this, db)
        ChallengesCommand.plugin = this
    }
}