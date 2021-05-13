/*
 * Copyright (c) 2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information.
 */
package com.dumbdogdiner.sass.api.event;

import com.dumbdogdiner.sass.api.reward.Challenge;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Called when a challenge is completed.
 */
public class ChallengeCompletedEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    /** The challenge associated with this event. */
    @NotNull
    @Getter
    private final Challenge challenge;

    /** The UUID of the player associated with this event. */
    @NotNull
    @Getter
    private final UUID playerId;

    public ChallengeCompletedEvent(
        @NotNull Challenge challenge,
        @NotNull UUID playerId
    ) {
        this.challenge = challenge;
        this.playerId = playerId;
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
