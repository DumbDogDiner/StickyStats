package com.dumbdogdiner.sass.stats.impl.event

import com.dumbdogdiner.sass.api.event.StatisticsEventHandler
import com.dumbdogdiner.sass.api.store.statistic.Statistic
import java.util.UUID
import java.util.function.BiConsumer

class StatisticsEventHandlerImpl(
    private val event: StatisticsEventImpl,
    private val action: BiConsumer<Statistic, UUID>,
) : StatisticsEventHandler {
    override fun getEvent() = event

    override fun execute(stat: Statistic, playerId: UUID) = action.accept(stat, playerId)

    override fun remove() = event.removeEventHandler(this)
}