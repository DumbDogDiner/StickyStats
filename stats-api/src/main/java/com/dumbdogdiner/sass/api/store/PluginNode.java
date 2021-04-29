package com.dumbdogdiner.sass.api.store;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Represents a node owned by a plugin.
 */
public interface PluginNode extends CategoryNode {
	/**
	 * @return The owner of this node.
	 */
	JavaPlugin getPlugin();
}
