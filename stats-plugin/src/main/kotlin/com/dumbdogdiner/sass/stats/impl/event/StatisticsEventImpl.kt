package com.dumbdogdiner.sass.stats.impl.event

import com.dumbdogdiner.sass.api.event.StatisticsEvent
import com.dumbdogdiner.sass.api.store.statistic.Statistic
import com.dumbdogdiner.sass.stats.impl.store.statistic.StatisticImpl
import java.util.UUID
import java.util.function.BiConsumer

class StatisticsEventImpl(private val stat: StatisticImpl) : StatisticsEvent {
    private val handlers = mutableListOf<StatisticsEventHandlerImpl>()

    override fun getHandlers() = handlers

    override fun create(action: BiConsumer<Statistic, UUID>): StatisticsEventHandlerImpl {
        val newHandler = StatisticsEventHandlerImpl(this, action)
        handlers += newHandler
        return newHandler
    }

    fun removeEventHandler(handler: StatisticsEventHandlerImpl) {
        handlers -= handler
        if (handlers.isEmpty()) {
            stat.deleteEvent()
        }
    }
}