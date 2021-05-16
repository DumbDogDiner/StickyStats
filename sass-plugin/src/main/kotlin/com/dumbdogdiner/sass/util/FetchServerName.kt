package com.dumbdogdiner.sass.util

import com.dumbdogdiner.sass.SassPlugin
import com.google.common.io.ByteStreams
import org.bukkit.Bukkit
import org.bukkit.plugin.messaging.PluginMessageListener

private const val BUNGEECORD_CHANNEL = "BungeeCord"

/**
 * Gets the server name by communicating with BungeeCord.
 */
private fun fetchServerNameViaBungee() {
    // create listener
    var listener = null as PluginMessageListener?
    listener = PluginMessageListener listener@{ channel, _, message ->
        try {
            // must be on bungee channel
            if (channel != BUNGEECORD_CHANNEL) return@listener
            // must be GetServer message
            @Suppress("UnstableApiUsage")
            val stream = ByteStreams.newDataInput(message)
            if (stream.readUTF() != "GetServer") return@listener
            // get server name
            SassPlugin.instance.serverName = stream.readUTF()
        } finally {
            // unregister the listener
            SassPlugin.instance.server.messenger.also { messenger ->
                messenger.unregisterIncomingPluginChannel(SassPlugin.instance, BUNGEECORD_CHANNEL, listener!!)
                messenger.unregisterOutgoingPluginChannel(SassPlugin.instance, BUNGEECORD_CHANNEL)
            }
        }
    }
    // register the listener
    SassPlugin.instance.server.messenger.also { messenger ->
        messenger.registerIncomingPluginChannel(SassPlugin.instance, BUNGEECORD_CHANNEL, listener)
        messenger.registerOutgoingPluginChannel(SassPlugin.instance, BUNGEECORD_CHANNEL)
    }
    // send GetServer message
    @Suppress("UnstableApiUsage")
    val stream = ByteStreams.newDataOutput()
    stream.writeUTF("GetServer")
    SassPlugin.instance.server.sendPluginMessage(SassPlugin.instance, BUNGEECORD_CHANNEL, stream.toByteArray())
}

fun fetchServerName() {
    if (Bukkit.spigot().spigotConfig.getBoolean("settings.bungeecord", false)) {
        fetchServerNameViaBungee()
    }
}
