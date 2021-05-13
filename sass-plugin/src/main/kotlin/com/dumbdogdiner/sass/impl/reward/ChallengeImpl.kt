package com.dumbdogdiner.sass.impl.reward

import com.dumbdogdiner.sass.api.event.ChallengeCompletedEvent
import com.dumbdogdiner.sass.api.event.StatisticModifiedEvent
import com.dumbdogdiner.sass.api.reward.Challenge
import com.dumbdogdiner.sass.api.reward.Tier
import com.dumbdogdiner.sass.api.stats.store.statistic.Statistic
import com.google.gson.JsonElement
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import java.util.function.Function

class ChallengeImpl(
    private val store: ChallengeStoreImpl,
    private val identifier: String,
    private val name: String,
    private val tiers: Array<out Tier>,
    private val statistic: Statistic,
    private val progress: Function<JsonElement?, Int>,
) : Challenge, Listener {
    private var invalid = false

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

    override fun getName(): String {
        ensureValid()
        return name
    }

    override fun getTiers(): Array<out Tier> {
        ensureValid()
        return tiers
    }

    override fun getProgress(): Function<JsonElement?, Int> {
        ensureValid()
        return progress
    }

    override fun getStatistic(): Statistic {
        ensureValid()
        return statistic
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
        if (event.statistic == statistic) {
            val oldTier = getTierForProgress(progress.apply(event.oldValue))
            val newTier = getTierForProgress(progress.apply(event.newValue))
            // if the old tier was not the "completed" tier...
            if (oldTier != -1) {
                // either the player has completed all tiers, or the player has moved to a greater tier.
                if (newTier == -1 || newTier > oldTier) {
                    // give reward
                    val playerId = event.playerId
                    val player = Bukkit.getOfflinePlayer(playerId)
                    val reward = tiers[oldTier].reward
                    RewardsAPIPluginImpl.economy.depositPlayer(player, reward.toDouble())
                    if (player is Player) {
                        player.sendMessage("For completing the $name challenge, you have been awarded $reward miles!")
                    }
                    Bukkit.getPluginManager().callEvent(ChallengeCompletedEvent(this, playerId))
                }
            }
        }
    }
}