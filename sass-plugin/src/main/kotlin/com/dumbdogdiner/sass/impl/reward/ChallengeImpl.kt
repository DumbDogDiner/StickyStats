package com.dumbdogdiner.sass.impl.reward

import com.dumbdogdiner.sass.api.event.ChallengeCompletedEvent
import com.dumbdogdiner.sass.api.event.StatisticModifiedEvent
import com.dumbdogdiner.sass.api.reward.Challenge
import com.dumbdogdiner.sass.api.reward.Tier
import com.dumbdogdiner.sass.api.stats.Statistic
import com.dumbdogdiner.sass.impl.SassServiceImpl
import com.dumbdogdiner.sass.translation.L
import com.dumbdogdiner.sass.util.getNameFromAshcon
import com.dumbdogdiner.sass.util.romanNumeral
import com.dumbdogdiner.stickyapi.bukkit.util.SoundUtil
import com.google.gson.JsonElement
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.Sound
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
                // give rewards for all tiers completed
                val player = Bukkit.getOfflinePlayer(event.playerId)
                for (tierIndex in oldTier until (if (newTier == -1) tiers.size else newTier)) {
                    giveReward(tierIndex, player)
                }
                // worth extra celebration if all tiers are completed
                if (newTier == -1 && player is Player) {
                    player.sendMessage(L.Chat.challengeCompleted("name" to name))
                    // quietly, we don't want to jumpscare players
                    player.playSound(player.location, Sound.UI_TOAST_CHALLENGE_COMPLETE, 0.5F, 1F)
                }
            }
        }
    }

    private fun giveReward(tierIndex: Int, player: OfflinePlayer) {
        val reward = tiers[tierIndex].reward
        val username = player.name ?: getNameFromAshcon(player.uniqueId)
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/stickywallet:eco add $username $reward Miles")
        if (player is Player) {
            val tier = (tierIndex + 1).romanNumeral()
            player.sendMessage(L.Chat.tierCompleted("name" to name, "tier" to tier, "reward" to reward))
            SoundUtil.sendSuccess(player)
        }
        Bukkit.getPluginManager().callEvent(ChallengeCompletedEvent(this, player.uniqueId))
    }
}
