/*
 * Copyright (c) 2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information.
 */
package com.dumbdogdiner.sass.api.event;

import com.dumbdogdiner.sass.api.stats.store.statistic.Statistic;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * Called when a statistic is modified.
 */
public class StatisticModifiedEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    /** The statistic this event is associated with. */
    @NotNull
    @Getter
    private final Statistic statistic;

    /** The UUID of the player this event is associated with. */
    @NotNull
    @Getter
    private final UUID playerId;

    /** The old value of the statistic. Will be null if there was no old value. */
    @Nullable
    @Getter
    private final JsonElement oldValue;

    /** The new value of the statistic. */
    @NotNull
    @Getter
    private final JsonElement newValue;

    @Getter
    @Setter
    private boolean cancelled;

    public StatisticModifiedEvent(
        @NotNull Statistic statistic,
        @NotNull UUID playerId,
        @Nullable JsonElement oldValue,
        @NotNull JsonElement newValue
    ) {
        this.statistic = statistic;
        this.playerId = playerId;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * @param <T> The type of the data.
     * @param clazz The class object of T.
     * @return The old value of the statistic. Will be null if there was no old value.
     */
    public <T> T getOldValue(@NotNull Class<T> clazz) {
        var value = this.getOldValue();
        if (value == null) {
            return null;
        } else {
            return new Gson().fromJson(value, clazz);
        }
    }

    /**
     * @param <T> The type of the data.
     * @param clazz The class object of T.
     * @return The new value of the statistic.
     */
    @NotNull
    public <T> T getNewValue(@NotNull Class<T> clazz) {
        return new Gson().fromJson(this.getNewValue(), clazz);
    }
}
