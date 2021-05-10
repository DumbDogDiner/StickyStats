package com.dumbdogdiner.sass.api.store.statistic;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Represents a statistic that should only be modified by setting to true or resetting.
 */
public interface FlagStatistic extends MutableStatistic<Boolean> {
    /**
     * Set this statistic.
     * @param playerId The {@link UUID} of the player.
     */
    void set(@NotNull UUID playerId);
}