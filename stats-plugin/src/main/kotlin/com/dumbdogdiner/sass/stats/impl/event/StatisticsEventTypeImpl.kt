package com.dumbdogdiner.sass.stats.impl.event

import com.dumbdogdiner.sass.api.event.StatisticsEventType
import java.util.function.Consumer

class StatisticsEventTypeImpl<T>(
    private val identifier: String,
) : StatisticsEventType<T> {
    private var handlers = arrayOf<StatisticsEventHandlerImpl<T>>()

    override fun getIdentifier() = identifier

    override fun getHandlers() = handlers

    override fun create(action: Consumer<T>): StatisticsEventHandlerImpl<T> {
        synchronized(this) {
            val newHandler = StatisticsEventHandlerImpl(this, action)
            // grow the array by one
            @Suppress("unchecked_cast")
            handlers = handlers.copyOf(handlers.size + 1) as Array<StatisticsEventHandlerImpl<T>>
            // add the element to the end
            handlers[handlers.size - 1] = newHandler
            return@create newHandler
        }
    }

    fun removeEventHandler(handler: StatisticsEventHandlerImpl<T>) {
        synchronized(this) {
            // perform a swap remove
            val i = handlers.indexOf(handler)
            // handler is already not present, return
            if (i == -1) return
            // put last element where this element is
            handlers[i] = handlers.last()
            // shrink the array by one
            handlers = handlers.copyOfRange(0, handlers.size - 1)
        }
    }
}