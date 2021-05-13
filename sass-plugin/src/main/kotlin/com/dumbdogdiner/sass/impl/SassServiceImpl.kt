package com.dumbdogdiner.sass.impl

import com.dumbdogdiner.sass.api.SassService
import com.dumbdogdiner.sass.impl.reward.ChallengeStoreImpl
import com.dumbdogdiner.sass.impl.stats.StoreImpl
import com.google.common.collect.MapMaker
import org.bukkit.plugin.java.JavaPlugin

object SassServiceImpl : SassService {
    private val storeMap = MapMaker().weakValues().makeMap<Pair<JavaPlugin, String>, StoreImpl>()
    private val challengeStoreMap = mutableMapOf<JavaPlugin, ChallengeStoreImpl>()

    fun getAllChallengeStores() = challengeStoreMap.values

    override fun getStore(plugin: JavaPlugin, serverName: String): StoreImpl =
        storeMap.getOrPut(plugin to serverName) { StoreImpl(plugin, serverName) }

    override fun getChallengeStore(plugin: JavaPlugin) =
        challengeStoreMap.getOrPut(plugin) { ChallengeStoreImpl(plugin) }
}