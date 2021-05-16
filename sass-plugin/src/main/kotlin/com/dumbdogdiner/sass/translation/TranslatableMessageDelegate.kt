package com.dumbdogdiner.sass.translation

import kotlin.reflect.KProperty

internal class TranslatableMessageDelegate(path: String) {
    private val message = TranslatableMessage(L.messageFile.getString(path) ?: "<missing translation value for $path>")
    operator fun getValue(obj: Any, property: KProperty<*>) = message
}

internal fun msg(path: String) = TranslatableMessageDelegate(path)
