package com.dumbdogdiner.sass.goals;

import org.bukkit.entity.Entity;

// Maybe this should be kt?
public class MobDeathGoal {
    Class<Entity> entityType;
    int goal;
    int reward;
    boolean infinite;
    int goalMultiplier;
    int rewardMultiplier;
    int level;
}
