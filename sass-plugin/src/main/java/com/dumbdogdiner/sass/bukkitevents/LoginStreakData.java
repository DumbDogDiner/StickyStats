package com.dumbdogdiner.sass.bukkitevents;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

// temporary, to unfuck compile
// but lombok still no run???
public class LoginStreakData {
    public LoginStreakData() {
        timestamp = LoginStreak.getDaysFromInstant(Instant.now());
        streak = 0;
    }

    public LoginStreakData(long timestamp) {
        this.timestamp = timestamp;
        streak = 0;
    }

    private long timestamp;


    private int streak;

    public void incrementStreak(){
        streak++;
    }

    public void resetStreak() {
        streak = 0;
    }


    // i hate my life :(
    public int getStreak() {
        return streak;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
