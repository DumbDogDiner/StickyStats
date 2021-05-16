package com.dumbdogdiner.sass.translation

import org.bukkit.configuration.file.YamlConfiguration

/**
 * The language object.
 */
object L {
    // TODO multiple language support
    internal val messageFile = (this::class.java.getResourceAsStream("messages.yml")
        ?: throw IllegalStateException("Couldn't find the translation resources"))
        .use { YamlConfiguration.loadConfiguration(it.reader()) }

    object Chat {
        val tierCompleted by msg("chat.tier completed")
        val challengeCompleted by msg("chat.challenge completed")
    }

    object Gui {
        val title by msg("gui.title")

        val previousPage by msg("gui.previous page")
        val nextPage by msg("gui.next page")

        object Description {
            val name by msg("gui.description.name")
            val completedName by msg("gui.description.completed name")
            val reward by msg("gui.description.reward")
            val completion by msg("gui.description.completion")
            val progress by msg("gui.description.progress")
            val goal by msg("gui.description.goal")
        }
    }
}