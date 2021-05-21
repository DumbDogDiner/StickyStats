package com.dumbdogdiner.sass.bukkitevents;

import org.bukkit.event.Listener;

import java.util.HashMap;

public class MobKillListener implements Listener {
    HashMap<Class<?> /* Entity Type */, Number /* Reward miles */> rewardTable;

    public MobKillListener(){
        // todo get config

        // todo Get each entity
    }
}
