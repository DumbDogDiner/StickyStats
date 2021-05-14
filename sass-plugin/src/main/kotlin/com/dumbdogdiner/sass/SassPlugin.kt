package com.dumbdogdiner.sass

import com.dumbdogdiner.sass.api.SassService
import com.dumbdogdiner.sass.command.ChallengesCommand
import com.dumbdogdiner.sass.db.databaseInit
import com.dumbdogdiner.sass.impl.SassServiceImpl
import com.dumbdogdiner.sass.util.fetchServerName
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
    lateinit var economy: Economy
    var serverName = null as String?

    override fun onLoad() {
        instance = this

        CommandAPI.onLoad(CommandAPIConfig())

        Bukkit.getServicesManager().register(SassService::class.java, SassServiceImpl, this, ServicePriority.Lowest)
    }

    override fun onEnable() {
        CommandAPI.onEnable(this)
        CommandAPI.registerCommand(ChallengesCommand::class.java)

        // we need to find an econ provider in order to deal out rewards
        economy = server.servicesManager.getRegistration(Economy::class.java)?.provider
            ?: throw IllegalStateException("An economy provider is required for this plugin!")

        fetchServerName()

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

        databaseInit(db)

        Bukkit.getPluginManager().registerEvents(SassServiceImpl, this)
    }

    companion object {
        lateinit var instance: SassPlugin
    }
}