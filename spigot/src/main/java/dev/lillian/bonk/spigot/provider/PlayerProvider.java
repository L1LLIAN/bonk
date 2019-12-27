package dev.lillian.bonk.spigot.provider;

import dev.lillian.bonk.core.argument.CommandArg;
import dev.lillian.bonk.core.executor.CommandExecutor;
import dev.lillian.bonk.core.messages.Messages;
import dev.lillian.bonk.core.provider.ArgumentProvider;
import dev.lillian.bonk.spigot.SpigotMessages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.stream.Collectors;

public final class PlayerProvider implements ArgumentProvider<Player> {
    @NotNull
    @Override
    public Player provide(@NotNull CommandArg arg, @NotNull List<? extends Annotation> annotations) throws IllegalArgumentException {
        String name = arg.get();
        Player p = Bukkit.getServer().getPlayer(name);
        if (p != null) {
            return p;
        }
        throw new IllegalArgumentException(Messages.format(SpigotMessages.Providers.playerNotFound, name));
    }

    @NotNull
    @Override
    public String argumentDescription() {
        return "player";
    }

    @NotNull
    @Override
    public List<String> provideSuggestions(@NotNull CommandExecutor<?> executor, @NotNull String input) {
        return Bukkit.getServer().getOnlinePlayers()
                .stream()
                .map(player -> player.getName().toLowerCase())
                .filter(name -> input.length() == 0 || name.startsWith(input.toLowerCase()))
                .collect(Collectors.toList());
    }
}
