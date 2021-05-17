package com.dumbdogdiner.sass.util

import com.dumbdogdiner.stickyapi.common.cache.Cache
import com.dumbdogdiner.stickyapi.common.cache.Cacheable
import com.google.gson.Gson
import java.net.URL
import java.util.UUID

private class CacheEntry(val uuid: UUID, val username: String) : Cacheable {
    override fun getKey() = "$uuid"
}

private val cache = Cache(CacheEntry::class.java)

// giving a reward to an offline player? it's possible via the api, so although it might be an edge case, but it's a
// case nonetheless
// can remove this code once StickyWallet has an API

/**
 * Get a name of a player from their [playerId].
 */
fun getNameFromAshcon(playerId: UUID) = cache["$playerId"] ?: run {
    // Make query
    val response = URL("https://api.ashcon.app/mojang/v2/user/$playerId").openStream().use {
        // Use Gson to serialize what we need
        Gson().fromJson(it.reader(), CacheEntry::class.java)
    }
    // Cache the response and return it
    cache.update(response)
    response.username
}
