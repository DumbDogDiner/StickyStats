package com.dumbdogdiner.sass.structure;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

public class Reward {
    @Getter
    private boolean achived = false;


    // hacky temp im sorry!!
    // this should be something that can be dealt with by things that implement, and be an interface
    @Getter @Setter
    String rewardCmd = "/me I win uwu";

    void award(Player player){
        if(!achived){
            achived = true;
        }
        Bukkit.dispatchCommand(player, rewardCmd);
    }
}
