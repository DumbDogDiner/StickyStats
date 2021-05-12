/*
 * Copyright (c) 2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information.
 */
package com.dumbdogdiner.sass.reward.api;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;

public interface RewardsAPIPlugin {
    /**
     * @return The instantiated rewards service object, or null if the service is not registered.
     */
    @Nullable
    static RewardsAPIPlugin getInstance() {
        var provider = Bukkit
            .getServicesManager()
            .getRegistration(RewardsAPIPlugin.class);
        if (provider == null) {
            return null;
        } else {
            return provider.getProvider();
        }
    }
}
