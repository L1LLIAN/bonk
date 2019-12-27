package dev.lillian.bonk.core.parametric;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
public class BindingContainer<T> {
    private final Class<T> type;
    private final Set<CommandBinding<T>> bindings;

    public BindingContainer(Class<T> type) {
        this.type = type;
        this.bindings = new HashSet<>();
    }

    public BindingContainer(Class<T> type, Set<CommandBinding<T>> bindings) {
        this.type = type;
        this.bindings = bindings;
    }
}
