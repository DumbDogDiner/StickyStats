package com.dumbdogdiner.sass.gui

import com.dumbdogdiner.sass.SassPlugin
import com.dumbdogdiner.sass.api.reward.Tier
import com.dumbdogdiner.sass.impl.SassServiceImpl
import com.dumbdogdiner.sass.translation.L
import com.dumbdogdiner.sass.util.cancelTask
import com.dumbdogdiner.sass.util.romanNumeral
import com.dumbdogdiner.sass.util.runTask
import com.dumbdogdiner.sass.util.runTaskAsynchronously
import com.dumbdogdiner.sass.util.scheduleSyncRepeatingTask
import com.dumbdogdiner.stickyapi.bukkit.gui.GUI
import org.bukkit.Bukkit
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
    private var entries = arrayOf<ChallengeGUIEntry>()
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
                        val tierState = if (tierIndex >= 0) {
                            val last = if (tierIndex == 0) null else challenge.tiers[tierIndex - 1]
                            val current = challenge.tiers[tierIndex]
                            ChallengeGUIEntryTierState(tierIndex, last, current, progress)
                        } else null
                        ChallengeGUIEntry(challenge.name, tierState)
                    }.toTypedArray()
                }

                // Run this synchronously, because it interfaces with Bukkit
                runTask {
                    this.redrawPage()
                }
            }
        }
    }

    private fun redrawPage() {
        if (this.pageNumber > this.maxPageNumber) {
            this.pageNumber = this.maxPageNumber
        }

        val start = this.pageNumber * SLOTS_PER_PAGE
        val end = start + SLOTS_PER_PAGE

        for (i in start until end) {
            removeSlot(i % 9, (i / 9) % 5)
        }

        for (i in start until min(end, this.entries.size)) {
            val item = ItemStack(Material.STONE)
            val entry = this.entries[i]
            val tierState = entry.tierState
            if (tierState != null) {
                item.name = L.challengeNameAndTier("name" to entry.name, "tier" to (tierState.index + 1).romanNumeral())
                item.lore = listOf(
                    L.Description.reward("reward" to tierState.current.reward),
                    L.Description.completion("percentage" to tierState.percentage),
                    L.Description.progress("progress" to tierState.progress),
                    L.Description.goal("goal" to tierState.goal),
                )
            } else {
                item.name = entry.name
                item.lore = listOf(L.Description.completed())
            }
            this.addSlot(i % 9, (i / 9) % 5, item)
        }
    }

    private data class ChallengeGUIEntry(
        val name: String,
        val tierState: ChallengeGUIEntryTierState?,
    )

    private data class ChallengeGUIEntryTierState(
        val index: Int,
        val last: Tier?,
        val current: Tier,
        val progress: Int,
    ) {
        val start
            get() = this.last?.threshold ?: 0
        val goal
            get() = this.current.threshold
        val percentage
            get() = (100 * (this.progress - this.start).toDouble() / (this.goal - this.start)).toInt()
    }
}