package com.dumbdogdiner.sass.reward.impl

import com.dumbdogdiner.sass.reward.api.ChallengeStore
import org.bukkit.plugin.java.JavaPlugin
import java.util.UUID
import java.util.function.Function
import java.util.function.Predicate

class ChallengeStoreImpl(private val plugin: JavaPlugin) : ChallengeStore {
    private val challengeMap = mutableMapOf<String, ChallengeImpl>()

    override fun getPlugin() = plugin

    override fun get(id: String) = challengeMap[id]

    override fun createChallenge(
        id: String,
        name: String,
        visibility: Predicate<UUID>,
        reward: Function<UUID, Int>,
        progress: Function<UUID, Float>,
        progressString: Function<UUID, String>,
        goalString: Function<UUID, String>
    ): Boolean = id !in challengeMap && run {
        challengeMap[id] = ChallengeImpl(this, id, name, visibility, reward, progress, progressString, goalString)
        true
    }

    override fun getAllChallenges() = challengeMap.values.toSet()

    fun removeChallenge(id: String) {
        challengeMap -= id
    }
}