/*
 * Copyright (c) 2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information.
 */
package com.dumbdogdiner.sass.api.event;

import com.dumbdogdiner.sass.api.stats.Statistic;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import java.util.UUID;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a statistic is removed for a single player.
 */
public class StatisticRemovedEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    /** The statistic this event is associated with. */
    @NotNull
    @Getter
    private final Statistic statistic;

    /** The UUID of the player this event is associated with. */
    @NotNull
    @Getter
    private final UUID playerId;

    /** The old value of the statistic. */
    @NotNull
    @Getter
    private final JsonElement oldValue;

    public StatisticRemovedEvent(
        @NotNull Statistic statistic,
        @NotNull UUID playerId,
        @NotNull JsonElement oldValue
    ) {
        this.statistic = statistic;
        this.playerId = playerId;
        this.oldValue = oldValue;
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
     * @return The old value of the statistic.
     */
    public <T> T getOldValue(@NotNull Class<T> clazz) {
        return new Gson().fromJson(this.getOldValue(), clazz);
    }
}
