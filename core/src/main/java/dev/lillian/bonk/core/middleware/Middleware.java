package dev.lillian.bonk.core.middleware;

import dev.lillian.bonk.core.command.WrappedCommand;
import dev.lillian.bonk.core.executor.CommandExecutor;
import org.jetbrains.annotations.NotNull;

public interface Middleware<T> {
    @NotNull
    Result run(@NotNull CommandExecutor<T> executor, @NotNull WrappedCommand command);

    enum Result {
        OK, STOP_EXECUTION
    }
}
