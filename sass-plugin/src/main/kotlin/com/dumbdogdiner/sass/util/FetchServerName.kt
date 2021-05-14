package com.dumbdogdiner.sass.util

import com.dumbdogdiner.sass.SassPlugin
import com.google.common.io.ByteStreams
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import org.bukkit.Bukkit
import org.bukkit.plugin.messaging.PluginMessageListener

private const val BUNGEECORD_CHANNEL = "BungeeCord"

private fun fetchServerNameViaBungee(): String {
    // create channel to transmit result
    val result = Channel<String>()
    // create listener
    val listener = PluginMessageListener listener@{ channel, _, message ->
        // must be on bungee channel
        if (channel != BUNGEECORD_CHANNEL) return@listener
        // must be GetServer message
        @Suppress("UnstableApiUsage")
        val stream = ByteStreams.newDataInput(message)
        if (stream.readUTF() != "GetServer") return@listener
        // get server name
        val serverName = stream.readUTF()
        runBlocking {
            result.send(serverName)
        }
    }
    try {
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
        // return response
        return runBlocking { result.receive() }
    } finally {
        // unregister the listener
        SassPlugin.instance.server.messenger.also { messenger ->
            messenger.unregisterIncomingPluginChannel(SassPlugin.instance, BUNGEECORD_CHANNEL, listener)
            messenger.unregisterOutgoingPluginChannel(SassPlugin.instance, BUNGEECORD_CHANNEL)
        }
    }
}

fun fetchServerName() {
    if (Bukkit.spigot().spigotConfig.getBoolean("settings.bungeecord", false)) {
        SassPlugin.instance.serverName = fetchServerNameViaBungee()
    }
}
