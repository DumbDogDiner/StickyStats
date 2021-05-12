package com.dumbdogdiner.sass.stats.impl.event

import com.dumbdogdiner.sass.api.event.StatisticEventContext
import com.dumbdogdiner.sass.api.event.StatisticEventHandler
import java.util.function.Consumer

class StatisticEventHandlerImpl(
    private val event: StatisticEventImpl,
    private val action: Consumer<StatisticEventContext>,
) : StatisticEventHandler {
    override fun getEvent() = event

    override fun remove() = event.removeEventHandler(this)

    fun execute(ctx: StatisticEventContextImpl) = action.accept(ctx)
}