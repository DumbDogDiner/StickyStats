/*
 * Copyright (c) 2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information.
 */
package com.dumbdogdiner.sass.api.reward;

import com.dumbdogdiner.sass.api.stats.Statistic;
import com.google.gson.JsonElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

/**
 * Represents a challenge. Challenges are goals that a player may pursue. They contain various attributes that allow
 * players to choose challenges to complete, track their progress, and anticipate rewards.
 */
public interface Challenge {
    /**
     * @return The name of this challenge.
     */
    @NotNull
    String getName();

    /**
     * @return The tiers for this challenge.
     */
    @NotNull
    Tier[] getTiers();

    /**
     * @return The statistic associated with this challenge.
     */
    @NotNull
    Statistic getStatistic();

    /**
     * @return The function that determines an integral value a given player has for this challenge. The value this
     * function returns determines which tier the player is on.
     */
    @NotNull
    Function<@Nullable JsonElement, @NotNull Integer> getProgress();

    /**
     * @param progress The progress level to find a tier for.
     * @return The tier index for that progress level, or -1 if that level is beyond all tiers.
     */
    default int getTierForProgress(int progress) {
        var tiers = this.getTiers();
        for (var i = 0; i < tiers.length; ++i) {
            if (tiers[i].getThreshold() > progress) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Unregister this challenge. It will no longer give rewards or be listed in the challenge list.
     */
    void unregister();
}
