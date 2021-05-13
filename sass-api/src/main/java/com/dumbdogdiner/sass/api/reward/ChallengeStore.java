/*
 * Copyright (c) 2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information.
 */
package com.dumbdogdiner.sass.api.reward;

import com.dumbdogdiner.sass.api.stats.Statistic;
import com.google.gson.JsonElement;
import java.util.Set;
import java.util.function.Function;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents the challenges for a plugin.
 */
public interface ChallengeStore {
    /**
     * @return The plugin this challenge store belongs to.
     */
    @NotNull
    JavaPlugin getPlugin();

    /**
     * @param id The identifier of the challenge.
     * @return The challenge matching the given identifier, or null if none exists.
     */
    @Nullable
    Challenge get(@NotNull String id);

    /**
     * @param id {@link Challenge#getIdentifier()}
     * @param name {@link Challenge#getName()}
     * @param tiers {@link Challenge#getTiers()}
     * @param statistic {@link Challenge#getStatistic()}
     * @param progress {@link Challenge#getProgress()}
     * @return True if a challenge was created, false if one already exists with that identifier.
     */
    boolean createChallenge(
        @NotNull String id,
        @NotNull String name,
        @NotNull Tier[] tiers,
        @NotNull Statistic statistic,
        @NotNull Function<@Nullable JsonElement, @NotNull Integer> progress
    );

    /**
     * @return All of the registered challenges.
     */
    @NotNull
    Set<Challenge> getAllChallenges();
}
