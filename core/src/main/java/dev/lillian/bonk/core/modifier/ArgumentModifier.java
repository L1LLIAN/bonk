package dev.lillian.bonk.core.modifier;

import dev.lillian.bonk.core.command.CommandExecution;
import dev.lillian.bonk.core.parametric.CommandParameter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface ArgumentModifier<T> {
    Optional<T> modify(@NotNull CommandExecution execution, @NotNull CommandParameter commandParameter, @Nullable T argument) throws IllegalArgumentException;
}
