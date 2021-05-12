/*
 * Copyright (c) 2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information.
 */
package com.dumbdogdiner.sass.api.stats.store.statistic;

import com.dumbdogdiner.sass.api.stats.event.StatisticEvent;
import com.dumbdogdiner.sass.api.stats.store.Store;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a statistic.
 */
public interface Statistic {
    /**
     * @return The unique identifier of this statistic.
     */
    @NotNull
    String getIdentifier();

    /**
     * @return The store this statistic is stored in.
     */
    @NotNull
    Store getStore();

    /**
     * @return The event that gets fired when this statistic is modified.
     */
    @NotNull
    StatisticEvent getEvent();

    /**
     * Delete this statistic. Further use of this statistic must be avoided.
     */
    void delete();

    /**
     * @param playerId The {@link UUID} of the player.
     * @return The value of the statistic for a player, or null if it does not exist.
     */
    @Nullable
    JsonElement get(@NotNull UUID playerId);

    /**
     * @param <T> The type of data to get.
     * @param clazz The class object of T.
     * @param playerId The {@link UUID} of the player.
     * @return The value of the statistic for a player, or null if it does not exist.
     */
    @Nullable
    default <T> T get(@NotNull Class<T> clazz, @NotNull UUID playerId) {
        var value = this.get(playerId);
        if (value == null) {
            return null;
        } else {
            return new Gson().fromJson(value, clazz);
        }
    }

    /**
     * @param playerId The {@link UUID} of the player.
     * @param value The value to store for this player.
     */
    void set(@NotNull UUID playerId, @NotNull JsonElement value);

    /**
     * @param <T> The type of data to set.
     * @param clazz The class object of T.
     * @param playerId The {@link UUID} of the player.
     * @param value The value to store for this player.
     */
    default <T> void set(
        @NotNull Class<T> clazz,
        @NotNull UUID playerId,
        @NotNull T value
    ) {
        this.set(playerId, new Gson().toJsonTree(value, clazz));
    }

    /**
     * @param playerId The {@link UUID} of the player.
     * @return True if the player had data, false if there was no data to remove.
     */
    boolean remove(@NotNull UUID playerId);
}
