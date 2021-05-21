package com.dumbdogdiner.sass.goals;

import com.google.gson.JsonObject;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

// Maybe this should be kt?
public class MobDeathGoal {
    EntityType type;
    int goal;
    int reward;
    boolean infinite;
    int goalMultiplier;
    int rewardMultiplier;
    int level;
}
