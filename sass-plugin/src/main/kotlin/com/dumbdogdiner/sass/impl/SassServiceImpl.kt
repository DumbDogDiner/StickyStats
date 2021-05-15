package com.dumbdogdiner.sass.impl

import com.dumbdogdiner.sass.SassPlugin
import com.dumbdogdiner.sass.api.SassService
import com.dumbdogdiner.sass.api.event.StatisticModifiedEvent
import com.dumbdogdiner.sass.api.reward.Tier
import com.dumbdogdiner.sass.api.stats.Statistic
import com.dumbdogdiner.sass.impl.reward.ChallengeImpl
import com.dumbdogdiner.sass.impl.stats.StoreImpl
import com.google.common.collect.MapMaker
import com.google.gson.JsonElement
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import java.util.WeakHashMap
import java.util.function.Function

object SassServiceImpl : SassService, Listener {
    // weak-value map of stores
    private val globalStoreMap = MapMaker().weakValues().makeMap<JavaPlugin, StoreImpl>()
    private val serverStoreMap = MapMaker().weakValues().makeMap<JavaPlugin, StoreImpl>()

    // set of all registered challenges
    private val challenges = mutableSetOf<ChallengeImpl>()
    // map of statistics to connected challenges
    private val statisticToChallengesMap = WeakHashMap<Statistic, MutableList<ChallengeImpl>>()

    override fun getGlobalStore(plugin: JavaPlugin): StoreImpl =
        globalStoreMap.getOrPut(plugin) { StoreImpl(plugin, true) }

    override fun getServerStore(plugin: JavaPlugin) =
        SassPlugin.instance.serverName?.let { serverStoreMap.getOrPut(plugin) { StoreImpl(plugin, false) } }

    override fun createChallenge(
        name: String,
        icon: ItemStack,
        tiers: Array<out Tier>,
        statistic: Statistic,
        progress: Function<JsonElement?, Int>
    ): ChallengeImpl {
        // Check the tiers to make sure there's nothing weird going on... All tier thresholds should be positive and in
        // ascending order, and no rewards should be negative.
        var lastThreshold = 0
        for ((i, tier) in tiers.withIndex()) {
            if (tier.threshold < lastThreshold) {
                throw IllegalArgumentException("Tier ${i + 1} has a lower threshold than the one before it")
            }
            if (tier.reward < 0) {
                throw IllegalArgumentException("Tier ${i + 1} has a negative reward")
            }
            lastThreshold = tier.threshold
        }
        // Create the new challenge
        val result = ChallengeImpl(name, icon, tiers, statistic, progress)
        // Add it to the set of challenges
        challenges += result
        // Add it to a list of challenges connected to a statistic
        statisticToChallengesMap.getOrPut(statistic) { mutableListOf() } += result
        return result
    }

    @EventHandler
    private fun onStatisticModified(event: StatisticModifiedEvent) {
        statisticToChallengesMap[event.statistic]?.let { challenges ->
            for (challenge in challenges) {
                challenge.check(event)
            }
        }
    }

    fun unregister(challenge: ChallengeImpl) {
        challenges -= challenge
        statisticToChallengesMap[challenge.statistic]?.let { it -= challenge }
    }

    val allChallenges
        get() = challenges as Set<ChallengeImpl> // prevent accidentally mutating the set
}