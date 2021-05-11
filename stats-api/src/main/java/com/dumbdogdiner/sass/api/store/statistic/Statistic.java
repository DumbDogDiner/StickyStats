package com.dumbdogdiner.sass.api.store.statistic;

import java.util.UUID;

import com.dumbdogdiner.sass.api.store.Store;
import com.google.gson.JsonElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a statistic.
 */
public interface Statistic {
    /**
     * @return The unique identifier of this statistic.
     */
    @NotNull String getIdentifier();

    /**
     * @return The store this statistic is stored in.
     */
    @NotNull Store getStore();

    /**
     * Delete this statistic. Further use of this statistic must be avoided.
     */
    void delete();

    /**
     * @param playerId The {@link UUID} of the player.
     * @return The value of the statistic for a player, or null if it does not exist.
     */
    @Nullable JsonElement get(@NotNull UUID playerId);

    /**
     * @param playerId The {@link UUID} of the player.
     * @param value The value to store for this player.
     */
    void set(@NotNull UUID playerId, @NotNull JsonElement value);

    /**
     * @param playerId The {@link UUID} of the player.
     * @return True if the player had data, false if there was no data to remove.
     */
    boolean remove(@NotNull UUID playerId);
}
