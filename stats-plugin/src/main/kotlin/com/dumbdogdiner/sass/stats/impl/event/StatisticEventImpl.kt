package com.dumbdogdiner.sass.stats.impl.event

import com.dumbdogdiner.sass.stats.api.event.StatisticEvent
import com.dumbdogdiner.sass.stats.api.event.StatisticEventContext
import com.dumbdogdiner.sass.stats.impl.store.statistic.StatisticImpl
import java.util.function.Consumer

class StatisticEventImpl(private val stat: StatisticImpl) : StatisticEvent {
    private val handlers = mutableListOf<StatisticEventHandlerImpl>()

    override fun getHandlers() = handlers

    override fun create(action: Consumer<StatisticEventContext>): StatisticEventHandlerImpl {
        val newHandler = StatisticEventHandlerImpl(this, action)
        handlers += newHandler
        return newHandler
    }

    fun removeEventHandler(handler: StatisticEventHandlerImpl) {
        handlers -= handler
        if (handlers.isEmpty()) {
            stat.deleteEvent()
        }
    }
}