package dev.lillian.bonk.core.provider;

import dev.lillian.bonk.core.executor.CommandExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface SuggestionProvider {
    @Nullable
    List<String> provideSuggestions(@NotNull CommandExecutor<?> executor, @NotNull String input);
}
