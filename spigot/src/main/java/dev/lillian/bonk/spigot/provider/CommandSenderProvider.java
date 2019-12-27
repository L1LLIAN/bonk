package dev.lillian.bonk.spigot.provider;

import dev.lillian.bonk.core.argument.CommandArg;
import dev.lillian.bonk.core.executor.CommandExecutor;
import dev.lillian.bonk.core.provider.ArgumentProvider;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;

public final class CommandSenderProvider implements ArgumentProvider<CommandSender> {
    @Override
    public boolean consumesArgument() {
        return false;
    }

    @NotNull
    @Override
    public CommandSender provide(@NotNull CommandArg arg, @NotNull List<? extends Annotation> annotations) throws IllegalArgumentException {
        return (CommandSender) arg.getSender().getInstance();
    }

    @NotNull
    @Override
    public String argumentDescription() {
        return "sender";
    }

    @NotNull
    @Override
    public List<String> provideSuggestions(@NotNull CommandExecutor<?> executor, @NotNull String input) {
        return Collections.emptyList();
    }
}
