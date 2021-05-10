package com.dumbdogdiner.sass.api.store;

import org.jetbrains.annotations.NotNull;

/**
 * Represents the root statistic store.
 */
public interface Store extends Node<PluginNode> {
	default @NotNull String getFullIdentifier() {
		// don't check the parent as we don't have one
		return this.getIdentifier();
	}

	@Override
	default @NotNull Node<?> getParent() {
		// this is annoying but it makes it easier for nodes
		 throw new IllegalStateException("Cannot access the parent node of the root node!");
	}
}
