package com.dumbdogdiner.sass.impl.reward

import com.dumbdogdiner.sass.api.event.ChallengeCompletedEvent
import com.dumbdogdiner.sass.api.event.StatisticModifiedEvent
import com.dumbdogdiner.sass.api.reward.Challenge
import com.dumbdogdiner.sass.api.stats.store.statistic.Statistic
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import java.util.UUID
import java.util.function.Function

class ChallengeImpl(
    private val store: ChallengeStoreImpl,
    private val identifier: String,
    private val name: Function<UUID, String?>,
    private val reward: Function<UUID, Int>,
    private val start: Function<UUID, Int>,
    private val goal: Function<UUID, Int>,
    private val progress: Function<UUID, Int>,
) : Challenge, Listener {
    private var invalid = false
    private val associatedStatistics = mutableSetOf<Statistic>()

    init {
        Bukkit.getPluginManager().registerEvents(this, store.plugin)
    }

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

    override fun addAssociatedStatistic(stat: Statistic): Boolean {
        ensureValid()
        return associatedStatistics.add(stat)
    }

    override fun removeAssociatedStatistic(stat: Statistic): Boolean {
        ensureValid()
        return associatedStatistics.remove(stat)
    }

    override fun getAssociatedStatistics(): Set<Statistic> {
        ensureValid()
        return associatedStatistics
    }

    override fun delete() {
        ensureValid()
        store.removeChallenge(identifier)
        StatisticModifiedEvent.getHandlerList().unregister(this)
        invalid = true
    }

    private fun ensureValid() {
        if (invalid) throw IllegalStateException("Use of invalid statistic '$identifier'")
    }

    @EventHandler
    private fun onStatisticModified(event: StatisticModifiedEvent) {
        ensureValid()
        if (event.statistic in associatedStatistics) {
            check(event.playerId)
        }
    }

    private fun check(playerId: UUID) {
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
            Bukkit.getPluginManager().callEvent(
                ChallengeCompletedEvent(
                    this,
                    playerId
                )
            )
        }
    }
}