package com.dumbdogdiner.sass.stats.impl.store

import com.dumbdogdiner.sass.api.store.DirectoryNode
import org.bukkit.plugin.java.JavaPlugin

// TODO database integration
class DirectoryNodeImpl private constructor(
    private val id: String,
    private val plugin: JavaPlugin,
    private val parent: DirectoryNode,
) : DirectoryNode {
    private val children = mutableMapOf<String, DirectoryNode>()

    override fun getIdentifier() = id

    override fun getPlugin() = plugin

    override fun getParent() = parent

    override fun getChildren() = children.values.toTypedArray()

    override operator fun get(id: String) = children[id]

    override fun mkdir(id: String, plugin: JavaPlugin): DirectoryNode {
        val dir = DirectoryNodeImpl(id, plugin, this)
        // add the child to our children list
        children[id] = dir
        // return the new directory
        return dir
    }
}