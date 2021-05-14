package com.dumbdogdiner.sass.gui

import com.dumbdogdiner.sass.SassPlugin
import com.dumbdogdiner.sass.api.reward.Tier
import com.dumbdogdiner.sass.impl.SassServiceImpl
import com.dumbdogdiner.stickyapi.bukkit.gui.GUI
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack
import kotlin.math.min

private const val SLOTS_PER_PAGE = 9 * 5

class ChallengeGUI(private val player: Player) : GUI(6, "Challenges", SassPlugin.instance) {
    private var pageNumber = 0
    private var entries = arrayOf<ChallengeGUIEntry>()
    private val task: Int

    private val maxPageNumber
        get() = if (entries.isEmpty()) 0 else (entries.size - 1) / SLOTS_PER_PAGE

    private fun playPageTurnSound() =
        player.playSound(player.location, Sound.ITEM_BOOK_PAGE_TURN, 1F, 1F)

    private fun updateEntries() {
        entries = SassServiceImpl.allChallenges.map { challenge ->
            val progress = challenge.progress.apply(challenge.statistic[player.uniqueId])
            val tierIndex = challenge.getTierForProgress(progress)
            val tierState = if (tierIndex >= 0) ChallengeGUIEntryTierState(
                tierIndex,
                if (tierIndex == 0) null else challenge.tiers[tierIndex - 1],
                challenge.tiers[tierIndex],
                progress
            ) else null
            ChallengeGUIEntry(challenge.name, tierState)
        }.toTypedArray()

        redrawPage()
    }

    private fun redrawPage() {
        if (pageNumber > maxPageNumber) {
            pageNumber = maxPageNumber
        }

        (pageNumber * SLOTS_PER_PAGE until (pageNumber + 1) * SLOTS_PER_PAGE).forEach { i ->
            removeSlot(i % 9, (i / 9) % 5)
        }

        (pageNumber * SLOTS_PER_PAGE until min((pageNumber + 1) * SLOTS_PER_PAGE, entries.size)).forEach { i ->
            addSlot(i % 9, (i / 9) % 5, ItemStack(Material.STONE).apply {
                val entry = entries[i]
                itemMeta = itemMeta.apply { setDisplayName(entry.name) }
                entry.tierState?.let { tierState ->
                    lore = listOf(
                        "Tier: ${tierState.index + 1}",
                        "Reward: ${tierState.current.reward}",
                        "Completion: ${tierState.percentage}",
                        "Progress: ${tierState.progress}",
                        "Goal: ${tierState.goal}",
                    )
                } ?: run {
                    lore = listOf("Completed")
                }
            })
        }
    }

    init {
        addSlot(0, 5, ItemStack(Material.ARROW).apply {
            itemMeta = itemMeta.apply { setDisplayName("Previous page") }
        }) { _, _ ->
            if (pageNumber > 0) {
                pageNumber--
                playPageTurnSound()
                redrawPage()
            }
        }

        addSlot(8, 5, ItemStack(Material.ARROW).apply {
            itemMeta = itemMeta.apply { setDisplayName("Next page") }
        }) { _, _ ->
            if (pageNumber < maxPageNumber) {
                pageNumber++
                playPageTurnSound()
                redrawPage()
            }
        }

        updateEntries()

        task = Bukkit.getScheduler().scheduleSyncRepeatingTask(SassPlugin.instance, this::updateEntries, 100, 100)
    }

    override fun onInventoryClose(event: InventoryCloseEvent) {
        Bukkit.getScheduler().cancelTask(task)
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
            get() = last?.threshold ?: 0
        val goal
            get() = current.threshold
        val percentage
            get() = (100 * (progress - start).toDouble() / (goal - start)).toInt()
    }
}