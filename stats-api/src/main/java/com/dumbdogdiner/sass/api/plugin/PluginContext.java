package com.dumbdogdiner.sass.api.plugin;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Represents the statistic context for the target plugin.
 */
public interface PluginContext {
	/**
	 * @return The {@link JavaPlugin} this context is owned by.
	 */
	JavaPlugin getPlugin();
}
