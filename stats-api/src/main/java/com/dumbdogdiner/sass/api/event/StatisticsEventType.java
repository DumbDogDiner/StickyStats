/*
 * Copyright (c) 2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information.
 */
package com.dumbdogdiner.sass.api.event;

import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an event type related to a statistics interaction.
 * @param <T> The type of data sent to handlers when this event is fired.
 */
public interface StatisticsEventType<T> {
    /**
     * @return The identifier for this event type.
     */
    @NotNull
    String getIdentifier();

    /**
     * @return The handlers to be called when this event is fired.
     */
    @NotNull
    StatisticsEventHandler<T>[] getHandlers();

    /**
     * @param action The action this new event handler should perform.
     * @return A newly created event handler.
     */
    @NotNull
    StatisticsEventHandler<T> create(@NotNull Consumer<T> action);

    /**
     * Fire this event, triggering all event handlers.
     * @param ctx The data to send to each event handler.
     */
    default void fire(T ctx) {
        for (var handler : getHandlers()) {
            handler.execute(ctx);
        }
    }
}
