/*
 * Copyright (c) 2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information.
 */
package com.dumbdogdiner.sass.api;

import com.dumbdogdiner.sass.api.reward.ChallengeStore;
import com.dumbdogdiner.sass.api.stats.Store;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface SassService {
    /**
     * @return The instance of the SassService, or null if none is registered.
     */
    @Nullable
    static SassService get() {
        var servicesManager = Bukkit.getServicesManager();
        var provider = servicesManager.getRegistration(SassService.class);
        if (provider != null) {
            return provider.getProvider();
        } else {
            return null;
        }
    }

    /**
     * @param plugin The plugin to get statistics for.
     * @param serverName The server to get statistics for.
     * @return The statistics store attached to this plugin instance.
     */
    @NotNull
    Store getStore(@NotNull JavaPlugin plugin, @NotNull String serverName);

    /**
     * @param plugin The plugin the store belongs to.
     * @return The {@link ChallengeStore} for the given plugin.
     */
    @NotNull
    ChallengeStore getChallengeStore(@NotNull JavaPlugin plugin);
}
