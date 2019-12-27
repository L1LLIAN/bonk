package dev.lillian.bonk.spigot;

import dev.lillian.bonk.core.executor.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

final class SpigotCommandExecutor implements CommandExecutor<CommandSender> {
    private final CommandSender commandSender;

    SpigotCommandExecutor(@NotNull CommandSender commandSender) {
        this.commandSender = commandSender;
    }

    @NotNull
    @Override
    public String getName() {
        return commandSender.getName();
    }

    @Override
    public void sendMessage(@NotNull String message) {
        commandSender.sendMessage(message);
    }

    @NotNull
    @Override
    public CommandSender getInstance() {
        return commandSender;
    }
}
