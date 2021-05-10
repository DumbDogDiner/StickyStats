package com.dumbdogdiner.sass.api.store;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents an node in the database with an identifier, a plugin it belongs to, and potentially child nodes.
 * @param <T> The type of child nodes this node can contain.
 */
public interface Node<T extends Node<?>> {
	/**
	 * @return The identifier of this node.
	 */
	@NotNull String getIdentifier();

	/**
	 * @return The full, unique identifier of this node.
	 */
	default @NotNull String getFullIdentifier() {
		// recursively ascend through the node tree and append identifiers.
		return this.getParent().getFullIdentifier() + "." + this.getIdentifier();
	}

	/**
	 * @return The plugin that created this node.
	 */
	@NotNull JavaPlugin getPlugin();

	/**
	 * @return The parent node of this node.
 	 */
	@NotNull Node<?> getParent();

	/**
	 * @return The child nodes attached to this node.
	 */
	@NotNull T[] getChildren();

	/**
	 * @param id The identifier of the child node.
	 * @return The matching child node, or null if no such node exists.
	 */
	@Nullable T get(@NotNull String id);

	/**
	 * @param relativePath The relative path, separated by '.'
	 * @return The matching child node, or null if no such node exists.
	 */
	default @Nullable Node<?> resolve(@NotNull String relativePath) {
		Node<?> result = this;
		for (var id : relativePath.split("\\.")) {
			result = result.get(id);
			if (result == null) return null;
		}
		return result;
	}
}
