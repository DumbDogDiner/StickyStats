package com.dumbdogdiner.sass.translation

private val TEMPLATE_REGEX = Regex("\\{[^}]+?}")

class TranslatableMessage(private val template: String) {
    private val parts = listOf(0) + findTemplateParts() + listOf(template.length)

    private fun findTemplateParts() =
        TEMPLATE_REGEX.findAll(template).flatMap { listOf(it.range.first, it.range.last + 1) }

    operator fun invoke(vararg args: Pair<String, Any>): String {
        val argMap = args.toMap()
        val result = StringBuilder()
        var inTemplate = false
        for (i in 1 until parts.size) {
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

    // Allow messages to be converted directly to string, if they have no placeholders
    override fun toString() = template
}
