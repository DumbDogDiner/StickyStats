package com.dumbdogdiner.sass.api;

import com.dumbdogdiner.sass.api.store.Store;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Provides an API interface for creating statistics fields.
 */
public final class StatisticsAPI {
	private StatisticsAPI() {}

	/**
	 * @return The StatisticsAPI plugin instance.
	 */
	public static StatisticsAPIPlugin getInstance() {
		return StatisticsAPIPlugin.getInstance();
	}

	/**
	 * Register a new statistic field with the target value types.
	 * @param plugin The plugin registering the field
	 * @param handle The name of the field
	 * @param values The types of the fields
	 */
	public static void registerField(JavaPlugin plugin, String handle, Class<?>... values) {
		getInstance().registerField(plugin, values);
	}

	/**
	 * Register a new statistic field with a player value as the first parameter.
	 * @param plugin The plugin registering the field
	 * @param handle The name of the field
	 * @param values The types of the fields
	 */
	public static void registerPlayerField(JavaPlugin plugin, String handle, Class<?>... values) {
		getInstance().registerPlayerField(plugin, values);
	}

	/**
	 * @return The root-level statistics store.
	 */
	public static Store getStore() {
		return getInstance().getStore();
	}
}
