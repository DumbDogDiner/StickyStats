package com.dumbdogdiner.sass.api;

import com.dumbdogdiner.sass.api.exception.InvalidServiceException;
import com.dumbdogdiner.sass.api.store.Store;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

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
			throw new InvalidServiceException(null);
		}
		return provider.getProvider();
	}

	/**
	 * @param plugin The plugin to get statistics for.
	 * @return The statistics store attached to this plugin instance.
	 */
	Store getStore(JavaPlugin plugin);
}
