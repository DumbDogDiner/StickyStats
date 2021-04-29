package com.dumbdogdiner.sass.api.store;

import com.dumbdogdiner.sass.api.store.field.Field;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a statistics node.
 */
public interface Node {
	/**
	 * @return The identifier of this node.
	 */
	String getIdentifier();

	/**
	 * @return The full, unique identifier of this node.
	 */
	default String getFullIdentifier() {
		// recursively ascend through the node tree and append identifiers.
		return this.getParent().getFullIdentifier() + "." + this.getIdentifier();
	}

	/**
	 * @return The parent node of this
 	 */
	Node getParent();

	/**
	 * @return The child nodes attached to this node.
	 */
	List<Node> getChildren();

	/**
	 * @return The child category nodes attached to this node.
	 */
	default List<CategoryNode> getCategories() {
		List<CategoryNode> list = new ArrayList<>();
		for (Node t : this.getChildren()) {
			if (t instanceof CategoryNode) {
				list.add((CategoryNode) t);
			}
		}
		return list;
	}

	/**
	 * @return A list of attached fields on this node.
	 */
	List<Field<?>> getFields();
}
