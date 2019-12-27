package dev.lillian.bonk.core.provider;

import dev.lillian.bonk.core.argument.CommandArg;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.util.List;

public interface ArgumentProvider<T> extends SuggestionProvider {
    default boolean consumesArgument() {
        return true;
    }

    @Nullable
    T provide(@NotNull CommandArg arg, @NotNull List<? extends Annotation> annotations) throws IllegalArgumentException;

    @NotNull
    String argumentDescription();
}
