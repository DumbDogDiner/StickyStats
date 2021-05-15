package com.dumbdogdiner.sass.translation

import kotlin.reflect.KProperty

internal class TranslatableMessageDelegate(private val path: String) {
    operator fun getValue(obj: Any, property: KProperty<*>) =
        TranslatableMessage(L.messageFile.getString(path) ?: "<missing translation value for $path>")
}

internal fun msg(path: String) = TranslatableMessageDelegate(path)