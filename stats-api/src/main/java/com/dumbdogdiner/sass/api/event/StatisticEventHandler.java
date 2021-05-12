/*
 * Copyright (c) 2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information.
 */
package com.dumbdogdiner.sass.api.event;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a handler for a {@link StatisticEvent}.
 */
public interface StatisticEventHandler {
    /**
     * @return The {@link StatisticEvent} this handler is registered to.
     */
    @NotNull
    StatisticEvent getEvent();

    /**
     * Remove this event handler from the event type. It will no longer be called.
     */
    void remove();
}
