package dev.lillian.bonk.core.help;

import dev.lillian.bonk.core.command.CommandContainer;
import dev.lillian.bonk.core.executor.CommandExecutor;
import org.jetbrains.annotations.NotNull;

public interface HelpFormatter {
    void sendHelpFor(@NotNull CommandExecutor<?> sender, @NotNull CommandContainer container);
}
