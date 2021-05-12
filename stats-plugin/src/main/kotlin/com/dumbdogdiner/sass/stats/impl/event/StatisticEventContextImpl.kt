package com.dumbdogdiner.sass.stats.impl.event

import com.dumbdogdiner.sass.api.event.StatisticEventContext
import com.dumbdogdiner.sass.stats.impl.store.statistic.StatisticImpl
import com.google.gson.JsonElement
import java.util.UUID

class StatisticEventContextImpl(
    private val statistic: StatisticImpl,
    private val playerId: UUID,
    private val oldValue: JsonElement?,
    private val newValue: JsonElement,
) : StatisticEventContext {
    private var canceled = false

    override fun getStatistic() = statistic

    override fun getPlayerId() = playerId

    override fun getOldValue() = oldValue

    override fun getNewValue() = newValue

    override fun isCanceled() = canceled

    override fun setCanceled(value: Boolean) {
        canceled = value
    }

}