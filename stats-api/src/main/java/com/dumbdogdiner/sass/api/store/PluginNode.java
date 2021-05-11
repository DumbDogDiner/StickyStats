package com.dumbdogdiner.sass.api.store;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a node that houses all statistics owned by a plugin.
 */
public interface PluginNode extends ChildNode<DirectoryNode> {
    // TODO can this functionality be combined with DirectoryNode?

    /**
     * @param id The identifier of the new DirectoryNode.
     * @param plugin The plugin creating the new DirectoryNode.
     * @return A new DirectoryNode which is a child of this node.
     */
    DirectoryNode mkdir(@NotNull String id, @NotNull JavaPlugin plugin);
}
