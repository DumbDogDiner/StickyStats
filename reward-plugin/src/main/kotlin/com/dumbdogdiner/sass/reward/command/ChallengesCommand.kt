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
        val uuid = sender.uniqueId
        RewardsAPIPluginImpl.getAllStores().forEach { store ->
            store.allChallenges.forEach { challenge ->
                if (challenge.visibility.test(uuid)) {
                    sender.sendMessage(challenge.name)
                    val reward = challenge.reward.apply(uuid)
                    if (reward > 0) {
                        val percentage = floor(challenge.progress.apply(uuid) * 100).toInt().coerceIn(0..99)
                        val progressString = challenge.progressString.apply(uuid)
                        val goalString = challenge.goalString.apply(uuid)
                        sender.sendMessage("  Reward: $reward Miles")
                        sender.sendMessage("  Completion: $percentage%")
                        sender.sendMessage("  Progress: $progressString")
                        sender.sendMessage("  Goal: $goalString")
                    } else {
                        sender.sendMessage("  Unattainable")
                    }
                }
            }
        }
    }
}