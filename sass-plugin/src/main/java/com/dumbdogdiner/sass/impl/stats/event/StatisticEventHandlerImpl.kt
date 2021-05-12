package com.dumbdogdiner.sass.impl.stats.event

import com.dumbdogdiner.sass.api.stats.event.StatisticEventContext
import com.dumbdogdiner.sass.api.stats.event.StatisticEventHandler
import java.util.function.Consumer

class StatisticEventHandlerImpl(
    private val event: StatisticEventImpl,
    private val action: Consumer<StatisticEventContext>,
) : StatisticEventHandler {
    override fun getEvent() = event

    override fun remove() = event.removeEventHandler(this)

    fun execute(ctx: StatisticEventContextImpl) = action.accept(ctx)
}