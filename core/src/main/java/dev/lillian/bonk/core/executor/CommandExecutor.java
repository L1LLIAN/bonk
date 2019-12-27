package dev.lillian.bonk.core.executor;

import org.jetbrains.annotations.NotNull;

public interface CommandExecutor<T> {
    @NotNull
    String getName();

    void sendMessage(@NotNull String message);

    @NotNull
    T getInstance();
}
