package com.dumbdogdiner.sass.api.store.statistic;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Represents a statistic that should only be modified by incrementing or resetting.
 */
public interface IncrementingStatistic extends MutableStatistic<Integer> {
    /**
     * Increment this statistic.
     * @param playerId The {@link UUID} of the player.
     */
    void inc(@NotNull UUID playerId);
}