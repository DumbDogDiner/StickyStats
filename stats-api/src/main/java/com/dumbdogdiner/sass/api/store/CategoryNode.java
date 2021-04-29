package com.dumbdogdiner.sass.api.store;

import com.dumbdogdiner.sass.api.store.field.Field;

import java.util.ArrayList;
import java.util.List;

/**
 * A node that represents a category of statistics.
 */
public interface CategoryNode extends Node {
	@Override
	default List<Field<?>> getFields() {
		// categories have no fields attached, so return an empty list.
		return new ArrayList<>();
	}
}
