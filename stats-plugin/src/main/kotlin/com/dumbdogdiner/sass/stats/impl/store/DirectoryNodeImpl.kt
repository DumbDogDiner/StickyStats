package com.dumbdogdiner.sass.stats.impl.store

import com.dumbdogdiner.sass.api.store.DirectoryNode
import com.dumbdogdiner.sass.api.store.DirectoryStorableNode
import com.dumbdogdiner.sass.api.store.Node
import org.bukkit.plugin.java.JavaPlugin

// TODO database integration
class DirectoryNodeImpl private constructor(
    private val id: String,
    private val plugin: JavaPlugin,
    private val parent: DirectoryNode,
) : DirectoryNode {
    private val childrenMap = mutableMapOf<String, DirectoryNode>()
    private var children = arrayOf<DirectoryNode>()
    private var isValid = true

    override fun getIdentifier(): String {
        ensureValid()
        return id
    }

    override fun getPlugin(): JavaPlugin {
        ensureValid()
        return plugin
    }

    override fun getParent(): DirectoryNode {
        ensureValid()
        return parent
    }

    override fun unlink() {
        ensureValid()
        TODO()
    }

    override fun getChildren(): Array<DirectoryNode> {
        ensureValid()
        return children.clone()
    }

    override fun get(id: String): DirectoryNode? {
        ensureValid()
        return childrenMap[id]
    }

    override fun mkdir(id: String, plugin: JavaPlugin): DirectoryNode {
        ensureValid()
        if (id in childrenMap) throw IllegalStateException("")
        val dir = DirectoryNodeImpl(id, plugin, this)
        // add the child to our children list
        childrenMap[id] = dir
        // add the child to the array
        growChildren(dir)
        // return the new directory
        return dir
    }

    private fun growChildren(node: DirectoryNode) {
        val newChildren = children.copyOf(children.size + 1)
        newChildren[children.size] = node
        @Suppress("unchecked_cast")
        children = newChildren as Array<DirectoryNode>
    }

    private fun ensureValid() {
        if (!isValid) throw IllegalStateException("Use of unlinked node")
    }
}