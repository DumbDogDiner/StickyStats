/*
 * Copyright (c) 2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information.
 */
package com.dumbdogdiner.sass.api.event;

import com.dumbdogdiner.sass.api.store.statistic.Statistic;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents the data associated with a {@link StatisticEvent}.
 */
public interface StatisticEventContext {
    /**
     * @return The {@link Statistic} this event is associated with.
     */
    @NotNull
    Statistic getStatistic();

    /**
     * @return The {@link UUID} of the player this event is associated with.
     */
    @NotNull
    UUID getPlayerId();

    /**
     * @return The old value of the statistic. Will be null if there was no old value.
     */
    @Nullable
    JsonElement getOldValue();

    /**
     * @param <T> The type of the data.
     * @param clazz The class object of T.
     * @return The old value of the statistic. Will be null if there was no old value.
     */
    @Nullable
    default <T> T getOldValue(@NotNull Class<T> clazz) {
        var value = this.getOldValue();
        if (value == null) {
            return null;
        } else {
            return new Gson().fromJson(value, clazz);
        }
    }

    /**
     * @return The new value of the statistic.
     */
    @NotNull
    JsonElement getNewValue();

    /**
     * @param <T> The type of the data.
     * @param clazz The class object of T.
     * @return The new value of the statistic.
     */
    @NotNull
    default <T> T getNewValue(@NotNull Class<T> clazz) {
        return new Gson().fromJson(this.getNewValue(), clazz);
    }

    /**
     * @return True if the event is to be canceled.
     */
    boolean isCanceled();

    /**
     * Set whether the event is to be canceled. If canceled, the statistic change will not occur.
     */
    void setCanceled(boolean value);
}
