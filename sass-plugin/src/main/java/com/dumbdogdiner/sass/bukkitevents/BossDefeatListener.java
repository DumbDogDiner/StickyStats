package com.dumbdogdiner.sass.bukkitevents;

import com.dumbdogdiner.sass.SassPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Boss;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Wither;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class BossDefeatListener implements Listener {
    public BossDefeatListener() {
        enable();
    }

    public void enable() {
        Bukkit.getPluginManager().registerEvents(this, SassPlugin.instance);
    }

    public void disable() {
        HandlerList.unregisterAll(this);
    }


    /**
     * Listener for an event of an entity death.
     * @param event
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onBossDeath(EntityDeathEvent event){
        Entity e = event.getEntity();
        if(e instanceof Boss){
            if(e instanceof EnderDragon){
                // TODO: reward for enderdragon
            } else if(e instanceof Wither) {
                // TODO: reward for wither
            }
        }
    }
}
