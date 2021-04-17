package com.dumbdogdiner.sass.api;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public interface StatisticsAPIPlugin {
	/**
	 * Register the statistics service.
	 * @param plugin The plugin registering the service
	 * @param service The plugin's implementation of the service
	 */
	static void register(JavaPlugin plugin, StatisticsAPIPlugin service) {
		Bukkit.getServicesManager().register(StatisticsAPIPlugin.class, service, plugin, ServicePriority.Lowest);
	}

	/**
	 * Fetch the instantiated statistics service object.
	 * @return {@link StatisticsAPIPlugin}
	 */
	@NotNull
	static StatisticsAPIPlugin getInstance() {
		var provider = Bukkit.getServicesManager().getRegistration(StatisticsAPIPlugin.class);
		// just in case someone tries something wacky.
		if (provider == null) {
			throw new RuntimeException("Failed to fetch the statistics API - registration is invalid!");
		}
		return provider.getProvider();
	}

	/**
	 * Register a new statistic field with the target value types.
	 * @param plugin The plugin registering the field
	 * @param values The types of the fields
	 */
	void registerField(JavaPlugin plugin, Class<?>... values);

	/**
	 * Register a new statistic field with a player value as the first parameter.
	 * @param plugin The plugin registering the field
	 * @param values The types of the fields
	 */
	default void registerPlayerField(JavaPlugin plugin, Class<?>... values) {
		// hacky code to insert a player field into the parameter list.
		Class<?>[] params = new Class<?>[values.length + 1];
		System.arraycopy(values, 0, params, 1, values.length);
		params[0] = Player.class;
		// register as normal
		this.registerField(plugin, params);
	}
}
