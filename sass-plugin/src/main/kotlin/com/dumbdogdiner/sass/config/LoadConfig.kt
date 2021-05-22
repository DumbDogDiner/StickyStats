package com.dumbdogdiner.sass.config

import com.dumbdogdiner.sass.api.stats.Statistic
import com.dumbdogdiner.sass.impl.stats.StatisticImpl
import org.bukkit.entity.EntityType
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import java.util.UUID

// entities killed { mob type }

private abstract class GoalSpec(val name: String, vararg val parameters: String) {
    abstract fun createListener(stat: StatisticImpl, parameters: Array<String>): GoalHandler<*>
}

// TODO add to API, could be useful
private fun Statistic.increment(playerId: UUID) {
    this[Int::class.java, playerId] = (this[Int::class.java, playerId] ?: 0) + 1
}

private abstract class GoalHandler<T : Event> : Listener {
    abstract val eventType: Class<T>

    @EventHandler
    abstract fun onEvent(event: T)
}

private val goalSpecs = arrayOf(
    object : GoalSpec("entities killed", "mob type") {
        override fun createListener(stat: StatisticImpl, parameters: Array<String>): GoalHandler<*> {
            val mobType = EntityType.valueOf(parameters[0])

            return object : GoalHandler<EntityDeathEvent>() {
                override val eventType = EntityDeathEvent::class.java

                override fun onEvent(event: EntityDeathEvent) {
                    if (event.entity.type != mobType) return
                    val killer = event.entity.killer ?: return
                    stat.increment(killer.uniqueId)
                }
            }
        }
    }
)
