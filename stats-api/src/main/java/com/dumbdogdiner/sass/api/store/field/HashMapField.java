package com.dumbdogdiner.sass.api.store.field;

import java.util.HashMap;

/**
 * Represents a field that maps type <code>K</code> to type <code>V</code>.
 * @param <K>
 * @param <V>
 */
public interface HashMapField<K, V> extends Field<HashMap<K, V>> {
	/**
	 * Get an entry contained within the wrapped hash map.
	 * @param key The key of the entry
	 * @return A value of type <code>V</code>, if one exists.
	 */
	default V getEntry(K key) {
		return this.get().get(key);
	}
}
