package com.dumbdogdiner.sass.stats.impl.store

import com.dumbdogdiner.sass.api.store.DirectoryNode
import com.dumbdogdiner.sass.api.store.Node
import org.bukkit.plugin.java.JavaPlugin

class DirectoryNodeImpl : DirectoryNode {
    override fun getIdentifier(): String {
        TODO("Not yet implemented")
    }

    override fun getPlugin(): JavaPlugin {
        TODO("Not yet implemented")
    }

    override fun getParent(): Node<*> {
        TODO("Not yet implemented")
    }

    override fun getChildren(): Array<DirectoryNode> {
        TODO("Not yet implemented")
    }

    override fun get(id: String): DirectoryNode? {
        TODO("Not yet implemented")
    }
}