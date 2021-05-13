package com.dumbdogdiner.sass.impl.reward

import com.dumbdogdiner.sass.api.reward.RewardsAPIPlugin
import net.milkbowl.vault.economy.Economy
import org.bukkit.plugin.java.JavaPlugin

object RewardsAPIPluginImpl : RewardsAPIPlugin {
    private val storeMap = mutableMapOf<JavaPlugin, ChallengeStoreImpl>()

    override fun getChallengeStore(plugin: JavaPlugin) = storeMap.getOrPut(plugin) { ChallengeStoreImpl(plugin) }

    fun getAllStores() = storeMap.values

    lateinit var economy: Economy
}