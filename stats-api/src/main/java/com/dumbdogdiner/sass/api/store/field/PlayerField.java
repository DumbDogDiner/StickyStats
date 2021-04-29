package com.dumbdogdiner.sass.api.store.field;

import org.bukkit.entity.Player;

/**
 * Represents a field that maps a player to a specified value.
 * @param <V>
 */
public interface PlayerField<V> extends HashMapField<Player, V> {
}
