package com.dumbdogdiner.sass.impl.reward

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
) : com.dumbdogdiner.sass.api.reward.Challenge, Listener {
    private var invalid = false
    private val associatedStatistics = mutableSetOf<com.dumbdogdiner.sass.api.stats.store.statistic.Statistic>()

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

    override fun addAssociatedStatistic(stat: com.dumbdogdiner.sass.api.stats.store.statistic.Statistic): Boolean {
        ensureValid()
        return associatedStatistics.add(stat)
    }

    override fun removeAssociatedStatistic(stat: com.dumbdogdiner.sass.api.stats.store.statistic.Statistic): Boolean {
        ensureValid()
        return associatedStatistics.remove(stat)
    }

    override fun getAssociatedStatistics(): Set<com.dumbdogdiner.sass.api.stats.store.statistic.Statistic> {
        ensureValid()
        return associatedStatistics
    }

    override fun delete() {
        ensureValid()
        store.removeChallenge(identifier)
        com.dumbdogdiner.sass.api.event.StatisticModifiedEvent.getHandlerList().unregister(this)
        invalid = true
    }

    private fun ensureValid() {
        if (invalid) throw IllegalStateException("Use of invalid statistic '$identifier'")
    }

    @EventHandler
    private fun onStatisticModified(event: com.dumbdogdiner.sass.api.event.StatisticModifiedEvent) {
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
                com.dumbdogdiner.sass.api.event.ChallengeCompletedEvent(
                    this,
                    playerId
                )
            )
        }
    }
}