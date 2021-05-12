package com.dumbdogdiner.sass.reward.impl

import com.dumbdogdiner.sass.reward.api.Challenge
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.UUID
import java.util.function.Function
import java.util.function.Predicate

class ChallengeImpl(
    private val store: ChallengeStoreImpl,
    private val identifier: String,
    private var name: String,
    private var visibility: Predicate<UUID>,
    private var reward: Function<UUID, Int>,
    private var progress: Function<UUID, Float>,
    private var progressString: Function<UUID, String>,
    private var goalString: Function<UUID, String>,
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

    override fun getName(): String {
        ensureValid()
        return name
    }

    override fun setName(value: String) {
        ensureValid()
        name = value
    }

    override fun getVisibility(): Predicate<UUID> {
        ensureValid()
        return visibility
    }

    override fun setVisibility(value: Predicate<UUID>) {
        ensureValid()
        visibility = value
    }

    override fun getReward(): Function<UUID, Int> {
        ensureValid()
        return reward
    }

    override fun setReward(value: Function<UUID, Int>) {
        ensureValid()
        reward = value
    }

    override fun getProgress(): Function<UUID, Float> {
        ensureValid()
        return progress
    }

    override fun setProgress(value: Function<UUID, Float>) {
        ensureValid()
        progress = value
    }

    override fun getProgressString(): Function<UUID, String> {
        ensureValid()
        return progressString
    }

    override fun setProgressString(value: Function<UUID, String>) {
        ensureValid()
        progressString = value
    }

    override fun getGoalString(): Function<UUID, String> {
        ensureValid()
        return goalString
    }

    override fun setGoalString(value: Function<UUID, String>) {
        ensureValid()
        goalString = value
    }

    override fun reward(playerId: UUID) {
        val player = Bukkit.getOfflinePlayer(playerId)
        val reward = reward.apply(playerId)
        RewardsAPIPluginImpl.economy.depositPlayer(player, reward.toDouble())
        if (player is Player) {
            player.sendMessage("For completing the $name challenge, you have been awarded $reward miles!")
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