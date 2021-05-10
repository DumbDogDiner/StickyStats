package com.dumbdogdiner.sass.api.store;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a node that contains other nodes, and possibly statistics.
 */
public interface DirectoryNode extends DirectoryStorableNode<DirectoryStorableNode<?>> {
    /**
     * @param id The identifier of the new DirectoryNode.
     * @param plugin The plugin creating the new DirectoryNode.
     * @return A new DirectoryNode which is a child of this node.
     */
    DirectoryNode mkdir(@NotNull String id, @NotNull JavaPlugin plugin);
}
