/*
 * Copyright (c) 2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information.
 */
package api.reward;

import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents the challenges for a plugin.
 */
public interface ChallengeStore {
    /**
     * @return The plugin this challenge store belongs to.
     */
    @NotNull
    JavaPlugin getPlugin();

    /**
     * @param id The identifier of the challenge.
     * @return The challenge matching the given identifier, or null if none exists.
     */
    @Nullable
    Challenge get(@NotNull String id);

    /**
     * @param id The identifier of the new challenge.
     * @param name {@link Challenge#getName()}
     * @param reward {@link Challenge#getReward()}
     * @param start {@link Challenge#getStart()}
     * @param goal {@link Challenge#getGoal()}
     * @param progress {@link Challenge#getProgress()}
     * @return True if a challenge was created, false if one already exists with that identifier.
     */
    boolean createChallenge(
        @NotNull String id,
        @NotNull Function<@NotNull UUID, @Nullable String> name,
        @NotNull Function<@NotNull UUID, @NotNull Integer> reward,
        @NotNull Function<@NotNull UUID, @NotNull Integer> start,
        @NotNull Function<@NotNull UUID, @NotNull Integer> goal,
        @NotNull Function<@NotNull UUID, @NotNull Integer> progress
    );

    /**
     * @return All of the registered challenges.
     */
    @NotNull
    Set<Challenge> getAllChallenges();
}
