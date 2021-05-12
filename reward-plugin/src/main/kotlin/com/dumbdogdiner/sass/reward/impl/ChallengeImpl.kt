package com.dumbdogdiner.sass.reward.impl

import com.dumbdogdiner.sass.reward.api.Challenge
import com.dumbdogdiner.sass.reward.api.ChallengeCompletedEvent
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.UUID
import java.util.function.Function
import java.util.function.Predicate

class ChallengeImpl(
    private val store: ChallengeStoreImpl,
    private val identifier: String,
    private val name: Function<UUID, String?>,
    private val reward: Function<UUID, Int>,
    private val start: Function<UUID, Int>,
    private val goal: Function<UUID, Int>,
    private val progress: Function<UUID, Int>,
) : Challenge {
    private var invalid = false

    override fun getChallengeStore(): ChallengeStoreImpl {
        ensureValid()
        return store
    }

    override fun getIdentifier(): String {
        ensureValid()
        return identifier
    }

    override fun getName(): Function<UUID, String?> {
        ensureValid()
        return name
    }

    override fun getReward(): Function<UUID, Int> {
        ensureValid()
        return reward
    }

    override fun getStart(): Function<UUID, Int> {
        ensureValid()
        return start
    }

    override fun getGoal(): Function<UUID, Int> {
        ensureValid()
        return goal
    }

    override fun getProgress(): Function<UUID, Int> {
        ensureValid()
        return progress
    }

    override fun check(playerId: UUID) {
        ensureValid()
        if (progress.apply(playerId) >= goal.apply(playerId)) {
            // give reward
            val player = Bukkit.getOfflinePlayer(playerId)
            val name = name.apply(playerId)
            val reward = reward.apply(playerId)
            RewardsAPIPluginImpl.economy.depositPlayer(player, reward.toDouble())
            if (player is Player) {
                player.sendMessage("For completing the $name challenge, you have been awarded $reward miles!")
            }
            Bukkit.getPluginManager().callEvent(ChallengeCompletedEvent(this, playerId))
        }
    }

    override fun delete() {
        ensureValid()
        store.removeChallenge(identifier)
        invalid = true
    }

    private fun ensureValid() {
        if (invalid) throw IllegalStateException("Use of invalid statistic '$identifier'")
    }
}