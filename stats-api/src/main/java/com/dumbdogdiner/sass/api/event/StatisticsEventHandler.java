/*
 * Copyright (c) 2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information.
 */
package com.dumbdogdiner.sass.api.event;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a handler for a {@link StatisticsEventType}.
 * @param <T> The type of data received when this event handler is called.
 */
public interface StatisticsEventHandler<T> {
    /**
     * @return The {@link StatisticsEventType} this handler is registered to.
     */
    @NotNull
    StatisticsEventType<T> getEventType();

    /**
     * Remove this event handler from the event type. It will no longer be called.
     */
    void remove();

    /**
     * Execute this event handler.
     * @param ctx The data associated with this event.
     */
    void execute(T ctx);
}
