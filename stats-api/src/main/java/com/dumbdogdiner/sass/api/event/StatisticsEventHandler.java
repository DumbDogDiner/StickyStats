/*
 * Copyright (c) 2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information.
 */
package com.dumbdogdiner.sass.api.event;

import com.dumbdogdiner.sass.api.store.statistic.Statistic;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a handler for a {@link StatisticsEvent}.
 */
public interface StatisticsEventHandler {
    /**
     * @return The {@link StatisticsEvent} this handler is registered to.
     */
    @NotNull
    StatisticsEvent getEvent();

    /**
     * Remove this event handler from the event type. It will no longer be called.
     */
    void remove();

    /**
     * Execute this event handler.
     * @param stat The statistic that was modified.
     * @param playerId The player whose statistic was modified.
     */
    void execute(@NotNull Statistic stat, @NotNull UUID playerId);
}
