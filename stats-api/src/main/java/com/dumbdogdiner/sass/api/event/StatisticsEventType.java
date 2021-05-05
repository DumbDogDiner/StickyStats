package com.dumbdogdiner.sass.api.event;

/**
 * Represents an event type related to a statistics interaction.
 * @param <T> The type of data sent to handlers when this event is fired.
 */
public interface StatisticsEventType<T> {
    /**
     * @return The identifier for this event type.
     */
    String getIdentifier();

    /**
     * @return The handlers to be called when this event is fired.
     */
    StatisticsEventHandler<T>[] getHandlers();

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
