/*
 * Copyright (c) 2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information.
 */
package com.dumbdogdiner.sass.reward.api;

import java.util.UUID;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a challenge. Challenges are goals that a player may pursue. They contain various attributes that allow
 * players to choose challenges to complete, track their progress, and anticipate rewards.
 */
public interface Challenge {
    /**
     * @return The store this challenge belongs to.
     */
    @NotNull
    ChallengeStore getChallengeStore();

    /**
     * @return The identifier for this challenge.
     */
    @NotNull
    String getIdentifier();

    /**
     * @return The function that determines the friendly name of this challenge for a given player, or null if the
     * player should not see this challenge.
     */
    @NotNull
    Function<@NotNull UUID, @Nullable String> getName();

    /**
     * @return The function that determines the reward for a given player. The function should return a nonzero value,
     * indicating the number of miles to reward. Other values indicate that the reward is not attainable.
     */
    @NotNull
    Function<@NotNull UUID, @NotNull Integer> getReward();

    /**
     * @return The function that determines the start of the range that this challenge spans for a given player.
     */
    @NotNull
    Function<@NotNull UUID, @NotNull Integer> getStart();

    /**
     * @return The function that determines the end of the range that this challenge spans for a given player.
     */
    @NotNull
    Function<@NotNull UUID, @NotNull Integer> getGoal();

    /**
     * @return The function that determines an integral value a given player has for this challenge.
     * @see Challenge#getStart()
     * @see Challenge#getGoal()
     */
    @NotNull
    Function<@NotNull UUID, @NotNull Integer> getProgress();

    /**
     * Check this challenge for a given player, and give an award if the requirements are met.
     */
    void check(@NotNull UUID playerId);

    /**
     * Delete this challenge. Further use of this object is invalid.
     */
    void delete();
}
