package dev.lillian.bonk.velocity;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import dev.lillian.bonk.core.executor.CommandExecutor;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

final class VelocityCommandExecutor implements CommandExecutor<CommandSource> {
    private final CommandSource source;

    VelocityCommandExecutor(CommandSource source) {
        this.source = source;
    }

    @Override
    public @NotNull String getName() {
        if (source instanceof Player) {
            return ((Player) source).getUsername();
        }

        return "Console";
    }

    @Override
    public void sendMessage(@NotNull String message) {
        source.sendMessage(Component.text(message));
    }

    @Override
    public @NotNull CommandSource getInstance() {
        return source;
    }
}
