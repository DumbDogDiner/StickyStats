package com.dumbdogdiner.sass.util

/**
 * Represents a cached value whose cached value might be `null`.
 */
data class CachedNullable<T : Any>(val value: T?)
