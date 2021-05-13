package com.dumbdogdiner.sass.impl.reward

import com.dumbdogdiner.sass.api.reward.ChallengeStore
import com.dumbdogdiner.sass.api.reward.Tier
import com.dumbdogdiner.sass.api.stats.store.statistic.Statistic
import com.google.gson.JsonElement
import org.bukkit.plugin.java.JavaPlugin
import java.util.function.Function

class ChallengeStoreImpl(private val plugin: JavaPlugin) :
    ChallengeStore {
    private val challengeMap = mutableMapOf<String, ChallengeImpl>()

    override fun getPlugin() = plugin

    override fun get(id: String) = challengeMap[id]

    override fun createChallenge(
        id: String,
        name: String,
        tiers: Array<out Tier>,
        statistic: Statistic,
        progress: Function<JsonElement?, Int>
    ): Boolean = id !in challengeMap && run {
        challengeMap[id] = ChallengeImpl(this, id, name, tiers, statistic, progress)
        true
    }

    override fun getAllChallenges() = challengeMap.values.toSet()

    fun removeChallenge(id: String) {
        challengeMap -= id
    }
}