package com.dumbdogdiner.sass.impl

import com.dumbdogdiner.sass.api.SassService
import com.dumbdogdiner.sass.api.event.StatisticModifiedEvent
import com.dumbdogdiner.sass.api.reward.Tier
import com.dumbdogdiner.sass.api.stats.Statistic
import com.dumbdogdiner.sass.impl.reward.ChallengeImpl
import com.dumbdogdiner.sass.impl.stats.StoreImpl
import com.google.common.collect.MapMaker
import com.google.gson.JsonElement
import org.bukkit.Server
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import java.util.WeakHashMap
import java.util.function.Function

object SassServiceImpl : SassService, Listener {
    // weak-value map of stores
    private val storeMap = MapMaker().weakValues().makeMap<String, StoreImpl>()

    // set of all registered challenges
    private val challenges = mutableSetOf<ChallengeImpl>()
    // map of statistics to connected challenges
    private val statisticToChallengesMap = WeakHashMap<Statistic, MutableList<ChallengeImpl>>()

    override fun getStore(plugin: JavaPlugin, server: Server): StoreImpl =
        storeMap.getOrPut(statStoreKey(plugin, server)) { StoreImpl(plugin, server) }

    override fun createChallenge(
        name: String,
        tiers: Array<out Tier>,
        statistic: Statistic,
        progress: Function<JsonElement?, Int>
    ): ChallengeImpl {
        val result = ChallengeImpl(name, tiers, statistic, progress)
        challenges += result
        statisticToChallengesMap.getOrPut(statistic) { mutableListOf() } += result
        return result
    }

    private fun statStoreKey(plugin: JavaPlugin, server: Server) = "${plugin.name}:${server.name}"

    @EventHandler
    private fun onStatisticModified(event: StatisticModifiedEvent) {
        statisticToChallengesMap[event.statistic]?.let { challenges ->
            challenges.forEach { it.check(event) }
        }
    }

    fun unregister(challenge: ChallengeImpl) {
        challenges -= challenge
        statisticToChallengesMap[challenge.statistic]?.let { it -= challenge }
    }

    val allChallenges
        get() = challenges as Set<ChallengeImpl> // prevent accidentally mutating the set
}