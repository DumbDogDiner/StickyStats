package com.dumbdogdiner.sass.api.store;

import org.jetbrains.annotations.NotNull;

public interface ChildNode<T extends ChildNode<?>> extends Node<T> {
    /**
     * @return The parent node of this node.
     */
    @NotNull Node<?> getParent();

    @Override
    default @NotNull String getFullIdentifier() {
        return this.getParent().getFullIdentifier() + "." + this.getIdentifier();
    }

    /**
     * Unlink this node from the hierarchy. Further use of this node will result in {@link IllegalStateException}s being
     * thrown.
     */
    void unlink();
}
