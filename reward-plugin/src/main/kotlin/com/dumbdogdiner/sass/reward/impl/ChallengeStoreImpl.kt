package com.dumbdogdiner.sass.reward.impl

import com.dumbdogdiner.sass.reward.api.ChallengeStore
import org.bukkit.plugin.java.JavaPlugin

class ChallengeStoreImpl(private val plugin: JavaPlugin) : ChallengeStore {
    private val challengeMap = mutableMapOf<String, ChallengeImpl>()

    override fun getPlugin() = plugin

    override fun get(id: String) = challengeMap[id]

    override fun createChallenge(id: String) = id !in challengeMap && run {
        challengeMap[id] = ChallengeImpl(this, id)
        true
    }

    override fun getAllChallenges() = challengeMap.values.toSet()

    fun removeChallenge(id: String) {
        challengeMap -= id
    }
}