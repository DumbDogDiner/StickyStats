package com.dumbdogdiner.sass.impl.reward

import com.dumbdogdiner.sass.SassPlugin
import com.dumbdogdiner.sass.api.event.ChallengeCompletedEvent
import com.dumbdogdiner.sass.api.event.StatisticModifiedEvent
import com.dumbdogdiner.sass.api.reward.Challenge
import com.dumbdogdiner.sass.api.reward.Tier
import com.dumbdogdiner.sass.api.stats.Statistic
import com.dumbdogdiner.sass.impl.SassServiceImpl
import com.dumbdogdiner.sass.translation.L
import com.dumbdogdiner.sass.util.romanNumeral
import com.google.gson.JsonElement
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack
import java.util.function.Function

class ChallengeImpl(
    private val name: String,
    private val icon: ItemStack,
    private val tiers: Array<out Tier>,
    private val statistic: Statistic,
    private val progress: Function<JsonElement?, Int>,
) : Challenge, Listener {
    override fun getName() = name

    override fun getIcon() = icon

    override fun getTiers() = tiers

    override fun getProgress() = progress

    override fun getStatistic() = statistic

    override fun unregister() {
        SassServiceImpl.unregister(this)
    }

    fun check(event: StatisticModifiedEvent) {
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
                SassPlugin.instance.economy.depositPlayer(player, reward.toDouble())
                if (player is Player) {
                    val tier = (oldTier + 1).romanNumeral()
                    player.sendMessage(L.challengeCompleted("name" to name, "tier" to tier, "reward" to reward))
                }
                Bukkit.getPluginManager().callEvent(ChallengeCompletedEvent(this, playerId))
            }
        }
    }
}