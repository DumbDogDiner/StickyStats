package com.dumbdogdiner.sass.api.event;

/**
 * Represents a handler for a {@link StatisticsEventType}.
 * @param <T> The type of data received when this event handler is called.
 */
public interface StatisticsEventHandler<T> {
    /**
     * @return The {@link StatisticsEventType} this handler is registered to.
     */
    StatisticsEventType<T> getEventType();

    /**
     * Execute this event.
     * @param ctx The data associated with this event.
     */
    void execute(T ctx);
}
