package com.dumbdogdiner.sass.translation

import com.dumbdogdiner.stickyapi.common.config.providers.YamlProvider
import kotlin.reflect.KProperty

private val TEMPLATE_REGEX = Regex("\\{[^}]+?}")

object L {
    private val messageFile: YamlProvider

    init {
        // Get the translation resource
        val stream = this::class.java.getResourceAsStream("messages.yml")
            ?: throw IllegalStateException("Couldn't find the translation resources")
        // Read message file
        // TODO multiple language support
        messageFile = stream.use { YamlProvider(it) }
    }

    val challengeCompleted by msg("challenge completed")

    private fun msg(path: String) = TranslatableMessageDelegate(path)

    private class TranslatableMessageDelegate(private val path: String) {
        operator fun getValue(self: L, property: KProperty<*>) =
            TranslatableMessage(self.messageFile.getString(path) ?: "<missing translation value for $path>")
    }

    class TranslatableMessage(private val template: String) {
        private val parts = listOf(0) + findTemplateParts() + listOf(template.length)

        private fun findTemplateParts() =
            TEMPLATE_REGEX.findAll(template).flatMap { listOf(it.range.first, it.range.last + 1) }

        operator fun invoke(vararg args: Pair<String, Any>): String {
            val argMap = args.toMap()
            val result = StringBuilder()
            var inTemplate = false
            (1 until parts.size).forEach { i ->
                if (inTemplate) {
                    val name = template.substring(parts[i - 1] + 1 until parts[i] - 1)
                    argMap[name]?.let(result::append) ?: result.append("{$name}")
                } else {
                    result.append(template.substring(parts[i - 1] until parts[i]))
                }
                inTemplate = !inTemplate
            }
            return result.toString()
        }
    }
}