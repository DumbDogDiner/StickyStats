package com.dumbdogdiner.sass.command

import com.dumbdogdiner.sass.gui.ChallengeGUI
import dev.jorel.commandapi.annotations.Command
import dev.jorel.commandapi.annotations.Default
import org.bukkit.entity.Player

/**
 * The /challenges command opens a GUI to display the player's progress in all challenges.
 */
@Command("challenges")
object ChallengesCommand {
    @Default
    @JvmStatic
    fun rewardsList(player: Player) {
        ChallengeGUI(player).open(player)
    }
}
