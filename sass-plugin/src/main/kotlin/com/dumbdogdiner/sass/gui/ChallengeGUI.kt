package com.dumbdogdiner.sass.gui

import com.dumbdogdiner.sass.SassPlugin
import com.dumbdogdiner.sass.impl.SassServiceImpl
import com.dumbdogdiner.sass.impl.reward.ChallengeImpl
import com.dumbdogdiner.sass.translation.L
import com.dumbdogdiner.sass.util.cancelTask
import com.dumbdogdiner.sass.util.romanNumeral
import com.dumbdogdiner.sass.util.runTask
import com.dumbdogdiner.sass.util.runTaskAsynchronously
import com.dumbdogdiner.sass.util.scheduleSyncRepeatingTask
import com.dumbdogdiner.stickyapi.bukkit.gui.GUI
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack
import java.util.UUID
import kotlin.math.min

private const val SLOTS_PER_PAGE = 9 * 5

/**
 * The GUI that shows a player a list of challenges.
 */
class ChallengeGUI(private val player: Player) : GUI(6, L.Gui.title(), SassPlugin.instance) {
    /** The page the player is on. */
    private var pageNumber = 0
    /** The entries the player can see. */
    private var entries = arrayOf<ItemStack>()
    /** The task ID for our repeating task to update the entries. */
    private val task: Int
    /** The maximum page number for this GUI. */
    private val maxPageNumber
        get() = if (this.entries.isEmpty()) 0 else (this.entries.size - 1) / SLOTS_PER_PAGE

    init {
        // Previous page button at bottom left
        ItemStack(Material.ARROW).let { previousPage ->
            previousPage.name = L.Gui.previousPage()
            this.addSlot(0, 5, previousPage) { _, _ ->
                if (this.pageNumber > 0) {
                    this.turnLeft()
                }
            }
        }
        // Next page button at bottom right
        ItemStack(Material.ARROW).let { nextPage ->
            nextPage.name = L.Gui.nextPage()
            this.addSlot(8, 5, nextPage) { _, _ ->
                if (this.pageNumber < this.maxPageNumber) {
                    this.turnRight()
                }
            }
        }
        // Every 5 seconds, update the entries in this GUI
        this.task = scheduleSyncRepeatingTask(0, 100) {
            this.updateEntries()
        }
    }

    override fun onInventoryClose(event: InventoryCloseEvent) {
        // Cancel this task when the inventory is closed
        cancelTask(this.task)
    }

    /**
     * Turn to the previous page.
     */
    private fun turnLeft() {
        this.pageNumber--
        this.playPageTurnSound()
        this.redrawPage()
    }

    /**
     * Turn to the next page.
     */
    private fun turnRight() {
        this.pageNumber++
        this.playPageTurnSound()
        this.redrawPage()
    }

    /**
     * Play the page turn sound for the player.
     */
    private fun playPageTurnSound() = this.player.playSound(player.location, Sound.ITEM_BOOK_PAGE_TURN, 1F, 1F)

    /**
     * Update the entries in this GUI.
     */
    private fun updateEntries() {
        // TODO do I need these synchronized blocks? Am I writing too defensively?
        // Run this async, as to not block game execution
        runTaskAsynchronously {
            val playerId = this.player.uniqueId
            // Synchronize on the entries
            synchronized(this.entries) {
                val allChallenges = SassServiceImpl.allChallenges
                // Synchronize on the challenges
                synchronized(allChallenges) {
                    // sorted alphabetically
                    this.entries = allChallenges
                        .sortedBy(ChallengeImpl::getName)
                        .map { challenge -> challengeToEntry(challenge, playerId) }
                        .toTypedArray()
                }

                // Run this synchronously, because it interfaces with Bukkit
                runTask {
                    this.redrawPage()
                }
            }
        }
    }

    /**
     * Redraw the current page.
     */
    private fun redrawPage() {
        if (this.pageNumber > this.maxPageNumber) {
            this.pageNumber = this.maxPageNumber
        }

        val start = this.pageNumber * SLOTS_PER_PAGE
        val end = start + SLOTS_PER_PAGE

        for (i in start until end) {
            this.removeSlot(i % 9, (i / 9) % 5)
        }

        for (i in start until min(end, this.entries.size)) {
            this.addSlot(i % 9, (i / 9) % 5, this.entries[i])
        }
    }
}

/**
 * The display name of the item stack.
 */
private var ItemStack.name
    get() = this.itemMeta.displayName
    set(value) {
        this.itemMeta = this.itemMeta.apply { this.setDisplayName(value) }
    }

/**
 * Convert a [challenge] to an entry for a [playerId].
 */
private fun challengeToEntry(challenge: ChallengeImpl, playerId: UUID): ItemStack {
    // Create the item to use
    val item = challenge.icon.clone()
    // Get the progress for the player
    val progress = challenge.getProgressForPlayer(playerId)
    // Get the tier for the progress
    val tierIndex = challenge.getTierForProgress(progress)
    // When the challenge is not completed, add information about the current tier and progress, otherwise, display that
    // it is completed.
    if (tierIndex >= 0) {
        val currentTier = challenge.tiers[tierIndex]
        val start = if (tierIndex == 0) 0 else challenge.tiers[tierIndex - 1].threshold
        val goal = currentTier.threshold
        val percentage = (100 * (progress - start).toDouble() / (goal - start)).toInt()
        // Name with tier
        item.name = L.Gui.Description.name("name" to challenge.name, "tier" to (tierIndex + 1).romanNumeral())
        // Information about challenge in lore
        item.lore = listOf(
            L.Gui.Description.reward("reward" to currentTier.reward),
            L.Gui.Description.completion("percentage" to percentage),
            L.Gui.Description.progress("progress" to progress),
            L.Gui.Description.goal("goal" to goal),
        )
    } else {
        // Name without tier
        item.name = L.Gui.Description.completedName("name" to challenge.name)
    }
    return item
}