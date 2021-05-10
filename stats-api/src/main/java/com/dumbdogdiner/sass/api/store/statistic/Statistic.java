package com.dumbdogdiner.sass.api.store.statistic;

import java.util.UUID;

import com.dumbdogdiner.sass.api.store.DirectoryStorableNode;
import com.dumbdogdiner.sass.api.store.NeverNode;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a terminal node containing a statistic.
 * @param <T> The type of statistic this node contains.
 */
public interface Statistic<T> extends DirectoryStorableNode<NeverNode> {
    /**
     * @param playerId The {@link UUID} of the player.
     * @return The value of the statistic for a player, or the initial value if it does not exist.
     */
    @NotNull T get(@NotNull UUID playerId);
}
