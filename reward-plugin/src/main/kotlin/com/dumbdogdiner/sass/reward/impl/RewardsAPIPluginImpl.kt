package com.dumbdogdiner.sass.reward.impl

import com.dumbdogdiner.sass.reward.api.RewardsAPIPlugin
import org.bukkit.plugin.java.JavaPlugin

object RewardsAPIPluginImpl : RewardsAPIPlugin {
    private val storeMap = mutableMapOf<JavaPlugin, ChallengeStoreImpl>()

    override fun getChallengeStore(plugin: JavaPlugin) = storeMap.getOrPut(plugin) { ChallengeStoreImpl(plugin) }

    fun getAllStores() = storeMap.values
}