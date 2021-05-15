package com.dumbdogdiner.sass.translation

import org.bukkit.configuration.file.YamlConfiguration

object L {
    // TODO multiple language support
    internal val messageFile = (this::class.java.getResourceAsStream("messages.yml")
        ?: throw IllegalStateException("Couldn't find the translation resources"))
        .use { YamlConfiguration.loadConfiguration(it.reader()) }

    val challengeCompleted by msg("challenge completed")
    val challengeNameAndTier by msg("challenge name and tier")

    val previousPage by msg("previous page")
    val nextPage by msg("next page")

    val challengesGuiTitle by msg("challenges gui title")

    object Description {
        val reward by msg("description.reward")
        val completion by msg("description.completion")
        val progress by msg("description.progress")
        val goal by msg("description.goal")

        val completed by msg("description.completed")
    }
}