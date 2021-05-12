package com.dumbdogdiner.sass.impl.reward

import com.dumbdogdiner.sass.api.reward.ChallengeStore
import org.bukkit.plugin.java.JavaPlugin
import java.util.UUID
import java.util.function.Function

class ChallengeStoreImpl(private val plugin: JavaPlugin) :
    ChallengeStore {
    private val challengeMap = mutableMapOf<String, ChallengeImpl>()

    override fun getPlugin() = plugin

    override fun get(id: String) = challengeMap[id]

    override fun createChallenge(
        id: String,
        name: Function<UUID, String?>,
        reward: Function<UUID, Int>,
        start: Function<UUID, Int>,
        goal: Function<UUID, Int>,
        progress: Function<UUID, Int>
    ) = id !in challengeMap && run {
        challengeMap[id] = ChallengeImpl(this, id, name, reward, start, goal, progress)
        true
    }

    override fun getAllChallenges() = challengeMap.values.toSet()

    fun removeChallenge(id: String) {
        challengeMap -= id
    }
}