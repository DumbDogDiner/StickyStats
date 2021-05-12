/*
 * Copyright (c) 2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information.
 */
package com.dumbdogdiner.sass.api.reward;

import java.util.UUID;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a challenge is completed.
 */
public class ChallengeCompletedEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    @NotNull
    private final Challenge challenge;

    @NotNull
    private final UUID playerId;

    public ChallengeCompletedEvent(
        @NotNull Challenge challenge,
        @NotNull UUID playerId
    ) {
        this.challenge = challenge;
        this.playerId = playerId;
    }

    /**
     * @return The challenge associated with this event.
     */
    @NotNull
    public Challenge getChallenge() {
        return challenge;
    }

    /**
     * @return The UUID of the player associated with this event.
     */
    @NotNull
    public UUID getPlayerId() {
        return playerId;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
