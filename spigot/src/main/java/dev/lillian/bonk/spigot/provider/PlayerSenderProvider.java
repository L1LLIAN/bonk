package dev.lillian.bonk.spigot.provider;

import dev.lillian.bonk.core.argument.CommandArg;
import dev.lillian.bonk.core.executor.CommandExecutor;
import dev.lillian.bonk.core.messages.Messages;
import dev.lillian.bonk.core.provider.ArgumentProvider;
import dev.lillian.bonk.spigot.SpigotMessages;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;

public final class PlayerSenderProvider implements ArgumentProvider<Player> {
    @Override
    public boolean consumesArgument() {
        return false;
    }

    @Override
    public @NotNull Player provide(@NotNull CommandArg arg, @NotNull List<? extends Annotation> annotations) throws IllegalArgumentException {
        if (arg.getSender().getInstance() instanceof Player) {
            return (Player) arg.getSender().getInstance();
        }
        throw new IllegalArgumentException(Messages.format(SpigotMessages.playerOnly));
    }

    @NotNull
    @Override
    public String argumentDescription() {
        return "player sender";
    }

    @NotNull
    @Override
    public List<String> provideSuggestions(@NotNull CommandExecutor<?> executor, @NotNull String input) {
        return Collections.emptyList();
    }
}