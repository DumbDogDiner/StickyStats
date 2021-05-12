package com.dumbdogdiner.sass.reward.command

import com.dumbdogdiner.sass.reward.impl.RewardsAPIPluginImpl
import dev.jorel.commandapi.annotations.Command
import dev.jorel.commandapi.annotations.Default
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@Command("challenges")
object ChallengesCommand {
    @Default
    @JvmStatic
    fun rewardsList(sender: CommandSender) {
        sender.sendMessage("--- Challenges ---")
        val uuid = (sender as? Player)?.uniqueId
        RewardsAPIPluginImpl.getAllStores().forEach { store ->
            store.allChallenges.forEach { challenge ->
                if (uuid == null || challenge.visibility.test(uuid)) {
                    sender.sendMessage(challenge.name)
                }
            }
        }
    }
}