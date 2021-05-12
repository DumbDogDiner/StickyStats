/*
 * Copyright (c) 2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information.
 */
package com.dumbdogdiner.sass.api.event;

import java.util.List;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an event related to a statistics modification.
 */
public interface StatisticEvent {
    /**
     * @return The handlers to be called when this event is fired.
     */
    @NotNull
    List<StatisticEventHandler> getHandlers();

    /**
     * @param action The action this new event handler should perform.
     * @return A newly created event handler.
     */
    @NotNull
    StatisticEventHandler create(
        @NotNull Consumer<StatisticEventContext> action
    );
}
