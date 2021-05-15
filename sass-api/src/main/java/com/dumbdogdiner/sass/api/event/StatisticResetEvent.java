/*
 * Copyright (c) 2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information.
 */
package com.dumbdogdiner.sass.api.event;

import com.dumbdogdiner.sass.api.stats.Statistic;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a statistic is reset for all players.
 */
public class StatisticResetEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    /** The statistic this event is associated with. */
    @NotNull
    @Getter
    private final Statistic statistic;

    public StatisticResetEvent(@NotNull Statistic statistic) {
        this.statistic = statistic;
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
}
