/*
 * Copyright (c) 2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information.
 */
package com.dumbdogdiner.sass.api.reward;

import lombok.Getter;

/**
 * Represents a goal and reward for a challenge. A challenge may have several tiers.
 */
public class Tier {

    /**The value a player must reach in a challenge to achieve this tier. */
    @Getter
    private final int threshold;

    /** The reward a player receives for completing this tier of a challenge. */
    @Getter
    private final int reward;

    public Tier(int threshold, int reward) {
        this.threshold = threshold;
        this.reward = reward;
    }
}
