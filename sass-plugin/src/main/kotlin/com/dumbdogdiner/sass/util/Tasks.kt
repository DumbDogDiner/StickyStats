package com.dumbdogdiner.sass.util

import com.dumbdogdiner.sass.SassPlugin
import org.bukkit.Bukkit

fun runTask(task: () -> Unit) =
    Bukkit.getScheduler().runTask(SassPlugin.instance, task)

fun runTaskAsynchronously(task: () -> Unit) =
    Bukkit.getScheduler().runTaskAsynchronously(SassPlugin.instance, task)

fun scheduleSyncRepeatingTask(delay: Long, period: Long, task: () -> Unit) =
    Bukkit.getScheduler().scheduleSyncRepeatingTask(SassPlugin.instance, task, delay, period)

fun cancelTask(taskId: Int) = Bukkit.getScheduler().cancelTask(taskId)
