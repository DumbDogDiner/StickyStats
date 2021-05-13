package com.dumbdogdiner.sass.impl.reward

import net.milkbowl.vault.economy.Economy
import org.bukkit.plugin.java.JavaPlugin

object RewardsAPIPluginImpl : com.dumbdogdiner.sass.api.reward.RewardsAPIPlugin {
    private val storeMap = mutableMapOf<JavaPlugin, ChallengeStoreImpl>()

    override fun getChallengeStore(plugin: JavaPlugin) = storeMap.getOrPut(plugin) { ChallengeStoreImpl(plugin) }

    fun getAllStores() = storeMap.values

    lateinit var economy: Economy
}