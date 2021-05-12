/*
 * Copyright (c) 2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information.
 */
package com.dumbdogdiner.sass.stats.api;

import com.dumbdogdiner.sass.stats.api.store.Store;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface StatisticsAPIPlugin {
    /**
     * @return The instantiated statistics service object, or null if the service is not registered.
     */
    @Nullable
    static StatisticsAPIPlugin getInstance() {
        var provider = Bukkit
            .getServicesManager()
            .getRegistration(StatisticsAPIPlugin.class);
        if (provider == null) {
            return null;
        } else {
            return provider.getProvider();
        }
    }

    /**
     * @param plugin The plugin to get statistics for.
     * @param serverName The server to get statistics for.
     * @return The statistics store attached to this plugin instance.
     */
    @NotNull
    Store getStore(@NotNull JavaPlugin plugin, @NotNull String serverName);
}
