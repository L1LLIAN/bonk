package dev.lillian.bonk.velocity.provider;

import com.velocitypowered.api.command.CommandSource;
import dev.lillian.bonk.core.argument.CommandArg;
import dev.lillian.bonk.core.executor.CommandExecutor;
import dev.lillian.bonk.core.provider.ArgumentProvider;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;

public final class CommandSourceProvider implements ArgumentProvider<CommandSource> {
    @Override
    public boolean consumesArgument() {
        return false;
    }

    @Override
    public @NotNull CommandSource provide(@NotNull CommandArg arg, @NotNull List<? extends Annotation> annotations) throws IllegalArgumentException {
        return (CommandSource) arg.getSender().getInstance();
    }

    @Override
    public @NotNull String argumentDescription() {
        return "sender";
    }

    @Override
    public @NotNull List<String> provideSuggestions(@NotNull CommandExecutor<?> executor, @NotNull String input) {
        return Collections.emptyList();
    }
}
