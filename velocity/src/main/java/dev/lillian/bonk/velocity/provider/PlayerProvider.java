package dev.lillian.bonk.velocity.provider;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.lillian.bonk.core.argument.CommandArg;
import dev.lillian.bonk.core.executor.CommandExecutor;
import dev.lillian.bonk.core.messages.Messages;
import dev.lillian.bonk.core.provider.ArgumentProvider;
import dev.lillian.bonk.velocity.VelocityMessages;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

public final class PlayerProvider implements ArgumentProvider<Player> {
    private final ProxyServer proxyServer;

    public PlayerProvider(@NotNull ProxyServer proxyServer) {
        requireNonNull(proxyServer, "proxyServer");
        this.proxyServer = proxyServer;
    }

    @Override
    public @NotNull Player provide(@NotNull CommandArg arg, @NotNull List<? extends Annotation> annotations) throws IllegalArgumentException {
        Optional<Player> optionalPlayer = proxyServer.getPlayer(arg.get());
        if (optionalPlayer.isPresent()) {
            return optionalPlayer.get();
        }
        throw new IllegalArgumentException(Messages.format(VelocityMessages.Providers.playerNotFound, arg.get()));
    }

    @Override
    public @NotNull String argumentDescription() {
        return "player";
    }

    @Override
    public @NotNull List<String> provideSuggestions(@NotNull CommandExecutor<?> executor, @NotNull String input) {
        return proxyServer.getAllPlayers()
                .stream()
                .map(player -> player.getUsername().toLowerCase())
                .filter(name -> input.length() == 0 || name.startsWith(input.toLowerCase()))
                .collect(Collectors.toList());
    }
}
