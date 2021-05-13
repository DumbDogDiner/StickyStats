/*
 * Copyright (c) 2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information.
 */
package api.reward;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
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

    /**
     * @param plugin The plugin the store belongs to.
     * @return The {@link ChallengeStore} for the given plugin.
     */
    @NotNull
    ChallengeStore getChallengeStore(@NotNull JavaPlugin plugin);
}
