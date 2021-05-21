package com.dumbdogdiner.sass.bukkitevents;

import com.dumbdogdiner.sass.SassPlugin;
import com.dumbdogdiner.sass.api.SassService;
import com.dumbdogdiner.sass.api.stats.Statistic;
import com.dumbdogdiner.sass.impl.stats.StatisticImpl;
import com.dumbdogdiner.sass.statistic.PlayerLoginStreak;
import kotlin.Pair;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.Period;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.time.ZoneOffset.UTC;

public final class LoginStreak implements Listener {
    static final long MILLIS_PER_DAY = 24 * 60 * 60 * 1000;


    // This might need cleanup later, because its really gross
    private final Statistic stat = SassService.get().getGlobalStore(SassPlugin.instance).get("login streak");

    @EventHandler(priority = EventPriority.MONITOR)
    public void onLogin(PlayerLoginEvent e){
        UUID playerId = e.getPlayer().getUniqueId();
       updateStreak(playerId);
    }


    /**
     * Called on player logout; will update the player's last seen time, and award
     * points if necessary (I.E.if the login crossed days). Maybe we can do this in
     * a better way in the future....
     * @param e Logout event
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onLogout(PlayerQuitEvent e) {
        updateStreak(e.getPlayer().getUniqueId());
    }


    // can do, but also, do we really want to update the streak if the player just stays logged in? like if you're just
    // sitting there for several days should that count
    // can do
    // That should be a config option? or just ask stix for me plz

    // stix says it doesn't matter, and he hopes/doesn't think anyone will do that


    // known bug: this doesnt handle if a player is somehow logged in for over 24 hours, though it does handle
    // Fix: set a scheduled task to run at a time of day that goes through and runs this (ideally midnight?)
    private void updateStreak(UUID playerId){
        Instant now = Instant.now();
        LoginStreakData data = stat.get(LoginStreakData.class, playerId);
        if (data != null) {
            long differenceInDays = getDaysFromInstant(now) - data.getTimestamp();
            if(differenceInDays == 1){
                data.incrementStreak();
            } else if(differenceInDays > 1) {
                data.resetStreak();
            }
            data.setTimestamp(getDaysFromInstant(now));
        } else {
            data = new LoginStreakData(getDaysFromInstant(now));
        }
        stat.set(LoginStreakData.class, playerId, data);
    }

    public static long getDaysFromInstant(Instant i) {
        return i.truncatedTo(ChronoUnit.DAYS).toEpochMilli() / LoginStreak.MILLIS_PER_DAY;
    }

}

