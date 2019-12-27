package dev.lillian.bonk.spigot;

import dev.lillian.bonk.core.command.AbstractCommandRegistry;
import dev.lillian.bonk.core.command.CommandContainer;
import dev.lillian.bonk.core.command.WrappedCommand;
import dev.lillian.bonk.core.executor.CommandExecutor;
import dev.lillian.bonk.spigot.annotation.Permission;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

final class SpigotCommandContainer extends CommandContainer {
    SpigotCommandContainer(AbstractCommandRegistry<?> commandService, Object object, String name, Set<String> aliases, Map<String, WrappedCommand> commands) {
        super(commandService, object, name, aliases, commands);
    }

    private String getPermission() {
        if (defaultCommand == null) {
            return "";
        }

        for (Annotation annotation : defaultCommand.getAnnotations()) {
            if (annotation instanceof Permission) {
                return ((Permission) annotation).value();
            }
        }

        return "";
    }

    public final class SpigotCommand extends Command implements PluginIdentifiableCommand {
        private final SpigotCommandRegistry commandRegistry;

        public SpigotCommand(@NotNull SpigotCommandRegistry commandRegistry) {
            super(name, "", "/" + name, new ArrayList<>(aliases));
            this.commandRegistry = commandRegistry;

            if (defaultCommand != null) {
                setUsage("/" + name + " " + defaultCommand.getGeneratedUsage());
                setDescription(defaultCommand.getDescription());
                setPermission(SpigotCommandContainer.this.getPermission());
            }
        }

        @Override
        public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, String[] strings) {
            if (getName().equalsIgnoreCase(SpigotCommandContainer.this.getName())) {
                CommandExecutor<?> executor = new SpigotCommandExecutor(commandSender);
                return commandRegistry.executeCommand(executor, SpigotCommandContainer.this, s, strings);
            }
            return false;
        }

        @Override
        public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, String[] args) throws IllegalArgumentException {
            CommandExecutor<?> executor = new SpigotCommandExecutor(sender);
            return tabCompleter.onTabComplete(executor, getName(), args);
        }

        @Override
        public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, String[] args, Location location) throws IllegalArgumentException {
            CommandExecutor<?> executor = new SpigotCommandExecutor(sender);
            return tabCompleter.onTabComplete(executor, getName(), args);
        }

        @Override
        public @NotNull Plugin getPlugin() {
            return commandRegistry.getPlugin();
        }
    }
}
