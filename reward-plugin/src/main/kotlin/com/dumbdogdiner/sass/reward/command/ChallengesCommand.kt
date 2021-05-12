package com.dumbdogdiner.sass.reward.command

import com.dumbdogdiner.sass.reward.impl.RewardsAPIPluginImpl
import dev.jorel.commandapi.annotations.Command
import dev.jorel.commandapi.annotations.Default
import org.bukkit.command.CommandSender

@Command("challenges")
object ChallengesCommand {
    @Default
    @JvmStatic
    fun rewardsList(sender: CommandSender) {
        sender.sendMessage("--- Challenges ---")
        RewardsAPIPluginImpl.getAllStores().forEach { store ->
            store.allChallenges.forEach { challenge ->
                sender.sendMessage(challenge.name)
            }
        }
    }
}