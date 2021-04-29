package com.dumbdogdiner.sass.api.store.field;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Represents a field storing a statistic.
 * @param <T> The type of the statistic
 */
public interface Field<T> {
	T get();

	/**
	 * @return The plugin that instantiated this field.
	 */
	JavaPlugin getPlugin();

	/**
	 * @return The unique string identifier of this field.
	 */
	String getIdentifier();
}
