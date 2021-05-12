/*
 * Copyright (c) 2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information.
 */
package com.dumbdogdiner.sass.reward.api;

import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;
import org.jetbrains.annotations.NotNull;

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
     * @return The friendly name of this challenge.
     */
    @NotNull
    String getName();

    /**
     * @param value The new friendly name of this challenge.
     */
    void setName(@NotNull String value);

    /**
     * @return The function that determines if a given player may see this challenge.
     */
    @NotNull
    Predicate<UUID> getVisibility();

    /**
     * @param value The new function that determines if a given player may see this challenge.
     */
    void setVisibility(@NotNull Predicate<UUID> value);

    /**
     * @return The function that determines the reward for a given player. The function should return a nonzero value,
     * indicating the number of miles to reward. Other values indicate that the reward is not attainable.
     */
    @NotNull
    Function<UUID, Integer> getReward();

    /**
     * @param value The new function that determines the reward for a given player.
     * @see Challenge#getReward()
     */
    void setReward(@NotNull Function<UUID, Integer> value);

    /**
     * @return The function that determines the progress percentage displayed to the player. The function should return
     * a value in the range [0, 1). Values outside this range will be coerced into this range.
     */
    @NotNull
    Function<UUID, Float> getProgress();

    /**
     * @param value The new function that determines the progress percentage displayed to the player.
     * @see Challenge#getProgress()
     */
    void setProgress(@NotNull Function<UUID, Float> value);

    /**
     * @return The function that determines the progress string shown to the player. This should clearly indicate the
     * progress using applicable units and descriptions. For example, a challenge for number of ores mined might return
     * "58 ores".
     */
    @NotNull
    Function<UUID, String> getProgressString();

    /**
     * @param value The new function that determines the progress string shown to the player.
     * @see Challenge#getProgressString()
     */
    void setProgressString(@NotNull Function<UUID, String> value);

    /**
     * @return The function that determines the goal string shown to the player. This should clearly indicate the goal
     * using applicable units and descriptions. For example, a challenge for number of ores mined, with a goal of 100
     * ores mined, might return "100 ores".
     */
    @NotNull
    Function<UUID, String> getGoalString();

    /**
     * @param value The new function that determines the goal string shown to the player.
     * @see Challenge#getGoalString()
     */
    void setGoalString(@NotNull Function<UUID, String> value);

    /**
     * @param playerId The UUID of the player to reward for completing the challenge.
     */
    void reward(@NotNull UUID playerId);

    /**
     * Delete this challenge. Further use of this object is invalid.
     */
    void delete();
}
