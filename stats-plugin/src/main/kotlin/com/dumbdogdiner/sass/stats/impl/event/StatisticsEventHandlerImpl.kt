package com.dumbdogdiner.sass.stats.impl.event

import com.dumbdogdiner.sass.api.event.StatisticsEventHandler
import java.util.function.Consumer

class StatisticsEventHandlerImpl<T>(
    private val eventType: StatisticsEventTypeImpl<T>,
    private val action: Consumer<T>
) : StatisticsEventHandler<T> {
    override fun getEventType() = eventType

    override fun execute(ctx: T) = action.accept(ctx)

    override fun remove() = eventType.removeEventHandler(this)
}