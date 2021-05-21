package com.dumbdogdiner.sass.statistic;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import static java.time.ZoneOffset.UTC;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

// Probably store this in its own table?
public class PlayerLoginStreak {
    public PlayerLoginStreak(UUID playerId) {
        this.playerId = playerId;
        lastLogin = Instant.now();
    }
    private final UUID playerId;

    private Instant lastLogin;
    private Instant nextLoginNotAfterForReward;

    @Setter(value = AccessLevel.NONE)
    private int loginStreak;

    public void onLogin() {
        int day = lastLogin.atOffset(UTC).getDayOfYear();
    }
}
