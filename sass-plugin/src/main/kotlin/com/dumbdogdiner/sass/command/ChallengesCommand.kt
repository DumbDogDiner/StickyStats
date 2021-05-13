package com.dumbdogdiner.sass.command

import com.dumbdogdiner.sass.SassPlugin
import com.dumbdogdiner.sass.impl.SassServiceImpl
import dev.jorel.commandapi.annotations.Command
import dev.jorel.commandapi.annotations.Default
import org.bukkit.Bukkit
import org.bukkit.entity.Player

@Command("challenges")
object ChallengesCommand {
    @Default
    @JvmStatic
    fun rewardsList(sender: Player) {
        sender.sendMessage("--- Challenges ---")
        val playerId = sender.uniqueId
        Bukkit.getScheduler().runTaskAsynchronously(SassPlugin.instance) { ->
            SassServiceImpl.allChallenges.forEach { challenge ->
                val progress = challenge.progress.apply(challenge.statistic.get(playerId))
                val tierIndex = challenge.getTierForProgress(progress)
                sender.sendMessage(challenge.name)
                if (tierIndex == -1) {
                    sender.sendMessage("  Completed")
                } else {
                    val currentTier = challenge.tiers[tierIndex]
                    val start = if (tierIndex > 0) challenge.tiers[tierIndex - 1].threshold else 0
                    val goal = currentTier.threshold
                    val percentage = (100 * (progress - start).toDouble() / (goal - start)).toInt()
                    sender.sendMessage("  Tier: ${tierIndex + 1}")
                    sender.sendMessage("  Reward: ${currentTier.reward} Miles")
                    sender.sendMessage("  Completion: $percentage%")
                    sender.sendMessage("  Progress: $progress")
                    sender.sendMessage("  Goal: $goal")
                }
            }
        }
    }
}