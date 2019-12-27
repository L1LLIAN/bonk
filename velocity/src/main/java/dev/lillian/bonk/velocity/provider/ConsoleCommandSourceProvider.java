package dev.lillian.bonk.velocity.provider;

import com.velocitypowered.api.proxy.ConsoleCommandSource;
import dev.lillian.bonk.core.argument.CommandArg;
import dev.lillian.bonk.core.executor.CommandExecutor;
import dev.lillian.bonk.core.messages.Messages;
import dev.lillian.bonk.core.provider.ArgumentProvider;
import dev.lillian.bonk.velocity.VelocityMessages;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;

public final class ConsoleCommandSourceProvider implements ArgumentProvider<ConsoleCommandSource> {
    @Override
    public boolean consumesArgument() {
        return false;
    }

    @Override
    public @NotNull ConsoleCommandSource provide(@NotNull CommandArg arg, @NotNull List<? extends Annotation> annotations) throws IllegalArgumentException {
        if (!(arg.getSender().getInstance() instanceof ConsoleCommandSource)) {
            throw new IllegalArgumentException(Messages.format(VelocityMessages.consoleOnly));
        }
        return (ConsoleCommandSource) arg.getSender().getInstance();
    }

    @Override
    public @NotNull String argumentDescription() {
        return "console sender";
    }

    @Override
    public @NotNull List<String> provideSuggestions(@NotNull CommandExecutor<?> executor, @NotNull String input) {
        return Collections.emptyList();
    }
}
