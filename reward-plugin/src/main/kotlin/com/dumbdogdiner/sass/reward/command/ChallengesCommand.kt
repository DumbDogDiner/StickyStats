package com.dumbdogdiner.sass.reward.command

import com.dumbdogdiner.sass.reward.impl.RewardsAPIPluginImpl
import dev.jorel.commandapi.annotations.Command
import dev.jorel.commandapi.annotations.Default
import org.bukkit.entity.Player
import kotlin.math.floor

@Command("challenges")
object ChallengesCommand {
    @Default
    @JvmStatic
    fun rewardsList(sender: Player) {
        sender.sendMessage("--- Challenges ---")
        val playerId = sender.uniqueId
        RewardsAPIPluginImpl.getAllStores().forEach { store ->
            store.allChallenges.forEach { challenge ->
                val name = challenge.name.apply(playerId)
                name?.let {
                    sender.sendMessage(name)
                    val reward = challenge.reward.apply(playerId)
                    if (reward > 0) {
                        val start = challenge.start.apply(playerId)
                        val goal = challenge.goal.apply(playerId)
                        val progress = challenge.progress.apply(playerId)
                        val percentage = floor(100 * progress.toDouble() / (goal - start).toDouble())
                        sender.sendMessage("  Reward: $reward Miles")
                        sender.sendMessage("  Completion: $percentage%")
                        sender.sendMessage("  Progress: $progress")
                        sender.sendMessage("  Goal: $goal")
                    } else {
                        sender.sendMessage("  Unattainable")
                    }
                }
            }
        }
    }
}