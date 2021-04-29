package com.dumbdogdiner.sass.api.store;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the root statistic store.
 */
public interface Store extends Node {
	default String getFullIdentifier() {
		return "root";
	}

	@Override
	default Node getParent() {
		// this is annoying but it makes it easier for nodes
		throw new IllegalStateException("Cannot access the parent node of the root node!");
	}

	/**
	 * @return A list of plugin nodes contained within this store.
	 */
	default List<PluginNode> getPluginNodes() {
		List<PluginNode> list = new ArrayList<>();
		for (Node t : this.getChildren()) {
			if (t instanceof PluginNode) {
				list.add((PluginNode) t);
			}
		}
		return list;
	}
}
