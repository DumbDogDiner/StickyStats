package com.dumbdogdiner.sass.impl.reward

import com.dumbdogdiner.sass.api.reward.RewardsAPIPlugin
import net.milkbowl.vault.economy.Economy
import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.exposed.sql.Database

object RewardsAPIPluginImpl : RewardsAPIPlugin {
    private val storeMap = mutableMapOf<JavaPlugin, ChallengeStoreImpl>()

    override fun getChallengeStore(plugin: JavaPlugin) = storeMap.getOrPut(plugin) { ChallengeStoreImpl(plugin) }

    fun getAllStores() = storeMap.values

    lateinit var economy: Economy

    lateinit var db: Database
}