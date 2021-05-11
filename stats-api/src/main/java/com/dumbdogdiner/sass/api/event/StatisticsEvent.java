/*
 * Copyright (c) 2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information.
 */
package com.dumbdogdiner.sass.api.event;

import com.dumbdogdiner.sass.api.store.statistic.Statistic;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an event related to a statistics modification.
 */
public interface StatisticsEvent {
    /**
     * @return The handlers to be called when this event is fired.
     */
    @NotNull
    List<StatisticsEventHandler> getHandlers();

    /**
     * @param action The action this new event handler should perform.
     * @return A newly created event handler.
     */
    @NotNull
    StatisticsEventHandler create(@NotNull BiConsumer<Statistic, UUID> action);

    /**
     * Fire this event, triggering all event handlers.
     * @param stat The statistic that was modified.
     * @param playerId The player whose statistic was modified.
     */
    default void fire(@NotNull Statistic stat, @NotNull UUID playerId) {
        for (var handler : getHandlers()) {
            handler.execute(stat, playerId);
        }
    }
}
