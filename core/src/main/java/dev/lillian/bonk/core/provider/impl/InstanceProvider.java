package dev.lillian.bonk.core.provider.impl;

import dev.lillian.bonk.core.argument.CommandArg;
import dev.lillian.bonk.core.executor.CommandExecutor;
import dev.lillian.bonk.core.provider.ArgumentProvider;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;

public final class InstanceProvider<T> implements ArgumentProvider<T> {
    private final T instance;

    public InstanceProvider(T instance) {
        this.instance = instance;
    }

    @Override
    public boolean consumesArgument() {
        return false;
    }

    @Override
    public T provide(@NotNull CommandArg arg, @NotNull List<? extends Annotation> annotations) {
        return instance;
    }

    @NotNull
    @Override
    public String argumentDescription() {
        return instance.getClass().getSimpleName() + " (provided)";
    }

    @NotNull
    @Override
    public List<String> provideSuggestions(@NotNull CommandExecutor<?> executor, @NotNull String input) {
        return Collections.emptyList();
    }
}
