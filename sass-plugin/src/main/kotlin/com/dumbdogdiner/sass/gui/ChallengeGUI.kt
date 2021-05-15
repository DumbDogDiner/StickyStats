package com.dumbdogdiner.sass.gui

import com.dumbdogdiner.sass.SassPlugin
import com.dumbdogdiner.sass.impl.SassServiceImpl
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
import kotlin.math.min

private const val SLOTS_PER_PAGE = 9 * 5

// function to easily get/set display name of an item stack
private var ItemStack.name
    get() = this.itemMeta.displayName
    set(value) {
        this.itemMeta = this.itemMeta.apply { this.setDisplayName(value) }
    }

class ChallengeGUI(private val player: Player) : GUI(6, L.challengesGuiTitle(), SassPlugin.instance) {
    private var pageNumber = 0
    private var entries = arrayOf<ItemStack>()
    private val task: Int
    private val maxPageNumber
        get() = if (this.entries.isEmpty()) 0 else (this.entries.size - 1) / SLOTS_PER_PAGE

    init {
        // Previous page button at bottom left
        val previousPage = ItemStack(Material.ARROW)
        previousPage.name = L.previousPage()
        this.addSlot(0, 5, previousPage) { _, _ ->
            if (this.pageNumber > 0) {
                this.turnLeft()
            }
        }
        // Next page button at bottom right
        val nextPage = ItemStack(Material.ARROW)
        nextPage.name = L.nextPage()
        this.addSlot(8, 5, nextPage) { _, _ ->
            if (this.pageNumber < this.maxPageNumber) {
                this.turnRight()
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

    private fun turnLeft() {
        this.pageNumber--
        this.playPageTurnSound()
        this.redrawPage()
    }

    private fun turnRight() {
        this.pageNumber++
        this.playPageTurnSound()
        this.redrawPage()
    }

    private fun playPageTurnSound() =
        this.player.playSound(player.location, Sound.ITEM_BOOK_PAGE_TURN, 1F, 1F)

    private fun updateEntries() {
        // Run this async, as to not block game execution
        runTaskAsynchronously {
            val playerId = this.player.uniqueId
            synchronized(this.entries) {
                val allChallenges = SassServiceImpl.allChallenges
                synchronized(allChallenges) {
                    this.entries = allChallenges.map { challenge ->
                        val progress = challenge.getProgressForPlayer(playerId)
                        val tierIndex = challenge.getTierForProgress(progress)
                        val item = challenge.icon.clone()
                        if (tierIndex >= 0) {
                            val currentTier = challenge.tiers[tierIndex]
                            val start = if (tierIndex == 0) 0 else challenge.tiers[tierIndex - 1].threshold
                            val goal = currentTier.threshold
                            val percentage = (100 * (progress - start).toDouble() / (goal - start)).toInt()
                            item.name = L.challengeNameAndTier(
                                "name" to challenge.name,
                                "tier" to (tierIndex + 1).romanNumeral()
                            )
                            item.lore = listOf(
                                L.Description.reward("reward" to currentTier.reward),
                                L.Description.completion("percentage" to percentage),
                                L.Description.progress("progress" to progress),
                                L.Description.goal("goal" to goal),
                            )
                        } else {
                            item.name = challenge.name
                            item.lore = listOf(L.Description.completed())
                        }
                        item
                    }.toTypedArray()
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