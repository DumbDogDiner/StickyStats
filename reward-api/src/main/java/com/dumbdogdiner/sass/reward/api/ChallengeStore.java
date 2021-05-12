/*
 * Copyright (c) 2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information.
 */
package com.dumbdogdiner.sass.reward.api;

import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;
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
     * @param id The identifier of the new challenge.
     * @param name The name of the new challenge.
     * @param visibility The visibility of the new challenge.
     * @param reward {@link Challenge#getReward()}
     * @param progress {@link Challenge#getProgress()}
     * @param progressString {@link Challenge#getProgressString()}
     * @param goalString {@link Challenge#getGoalString()}
     * @return True if a challenge was created, false if one already exists with that identifier.
     */
    boolean createChallenge(
        @NotNull String id,
        @NotNull String name,
        @NotNull Predicate<UUID> visibility,
        @NotNull Function<UUID, Integer> reward,
        @NotNull Function<UUID, Float> progress,
        @NotNull Function<UUID, String> progressString,
        @NotNull Function<UUID, String> goalString
    );

    /**
     * @return All of the registered challenges.
     */
    @NotNull
    Set<Challenge> getAllChallenges();
}
