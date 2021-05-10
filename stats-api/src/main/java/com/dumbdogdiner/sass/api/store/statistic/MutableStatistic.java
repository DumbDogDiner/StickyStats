package com.dumbdogdiner.sass.api.store.statistic;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Represents a statistic that can be modified.
 */
public interface MutableStatistic<T> extends Statistic<T> {
    /**
     * Set this statistic.
     * @param playerId The {@link UUID} of the player.
     * @param value The value to store.
     */
    void set(@NotNull UUID playerId, @NotNull T value);

    /**
     * Resets the stat to the initial value.
     * @param playerId The {@link UUID} of the player.
     */
    void reset(@NotNull UUID playerId);

    /**
     * @return True if this statistic will reset daily.
     */
    boolean isDaily();

    /**
     * @return Statistics that are modified along with this one.
     */
    @NotNull MutableStatistic<T>[] getCascades();
}
