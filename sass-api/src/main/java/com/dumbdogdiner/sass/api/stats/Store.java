/*
 * Copyright (c) 2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information.
 */
package com.dumbdogdiner.sass.api.stats;

import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * Represents the set of statistics stored by a plugin.
 */
public interface Store {
    /**
     * @return The plugin this store belongs to.
     */
    @NotNull
    JavaPlugin getPlugin();

    /**
     * @return The server this store belongs to.
     */
    @NotNull
    Server getServer();

    /**
     * @param id The identifier of the statistic to fetch.
     * @return The statistic with the given identifier.
     */
    @NotNull
    Statistic get(@NotNull String id);
}
