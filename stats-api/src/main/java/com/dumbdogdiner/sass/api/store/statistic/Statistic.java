package com.dumbdogdiner.sass.api.store.statistic;

import java.util.Set;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a statistic.
 * @param <K> The type of object that groups this statistic, or the key
 * @param <V> The type of values of this statistic
 */
public interface Statistic<K, V> {
    /**
     * @param group The group to find a value for
     * @return The value for this statistic for the given group, or null if
     * this group has no value for this statistic.
     */
    @Nullable
    V get(K group);

    /**
     * @return True if this statistic has a value for this group, false
     * otherwise.
     */
    default boolean contains(K group) {
        return get(group) != null;
    }

    /**
     * @return A set of all of the entries in this statistic.
     */
    @NotNull
    Set<Entry<K, V>> getAll();

    /**
     * @return The plugin this statistic belongs to.
     */
    @NotNull
    JavaPlugin getPlugin();

    /**
     * @return The identifier of this statistic.
     */
    @NotNull
    String getIdentifier();

    /**
     * Represents an entry in a statistic.
     */
    interface Entry<K, V> {
        /**
         * @return The group of this statistic entry.
         */
        @NotNull
        K getGroup();

        /**
         * @return The value of this statistic entry.
         */
        @NotNull
        V getValue();
    }
}
