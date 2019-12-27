package dev.lillian.bonk.spigot;

import dev.lillian.bonk.core.annotation.Sender;
import dev.lillian.bonk.core.command.AbstractCommandRegistry;
import dev.lillian.bonk.core.command.CommandContainer;
import dev.lillian.bonk.core.command.WrappedCommand;
import dev.lillian.bonk.core.exception.CommandRegistrationException;
import dev.lillian.bonk.core.messages.Messages;
import dev.lillian.bonk.core.middleware.Middleware;
import dev.lillian.bonk.spigot.annotation.Permission;
import dev.lillian.bonk.spigot.middleware.SpigotMiddleware;
import dev.lillian.bonk.spigot.provider.CommandSenderProvider;
import dev.lillian.bonk.spigot.provider.ConsoleCommandSenderProvider;
import dev.lillian.bonk.spigot.provider.PlayerProvider;
import dev.lillian.bonk.spigot.provider.PlayerSenderProvider;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;

import static java.util.Objects.requireNonNull;

public final class SpigotCommandRegistry extends AbstractCommandRegistry<SpigotCommandContainer> {
    private final JavaPlugin plugin;
    private final SpigotReflection spigotReflection = new SpigotReflection(this);

    public SpigotCommandRegistry(@NotNull JavaPlugin plugin) {
        requireNonNull(plugin, "plugin");

        this.plugin = plugin;

        Messages.prefix = ChatColor.RED.toString();
        Messages.unknownSubCommand = "Unknown sub-command: {0}.  Use '/{1} help' to see available commands.";

        setHelpFormatter((executor, container) -> {
            if (executor.getInstance() instanceof CommandSender) {
                CommandSender sender = (CommandSender) executor.getInstance();
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m--------------------------------"));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bHelp &7- &6/" + container.getName()));
                for (WrappedCommand c : container.getCommands().values()) {
                    BaseComponent msg = new TextComponent(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&',
                            "&7/" + container.getName() + (c.getName().length() > 0 ? " &e" + c.getName() : "") + " &7" + c.getMostApplicableUsage() + "&7- &f" + c.getShortDescription()));
                    msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(ChatColor.GRAY + "/" + container.getName() + " " + c.getName() + "- " + ChatColor.WHITE + c.getDescription())));
                    msg.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/" + container.getName() + " " + c.getName()));
                    SpigotUtil.sendMessage(sender, msg);
                }
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m--------------------------------"));
            }
        });

        addDefaults();
    }

    @Override
    protected void runAsync(@NotNull Runnable runnable) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, runnable);
    }

    @Override
    protected void addDefaults() {
        bind(CommandSender.class).annotatedWith(Sender.class).toProvider(new CommandSenderProvider());
        bind(Player.class).annotatedWith(Sender.class).toProvider(new PlayerSenderProvider());
        bind(ConsoleCommandSender.class).annotatedWith(Sender.class).toProvider(new ConsoleCommandSenderProvider());

        bind(Player.class).toProvider(new PlayerProvider());

        registerMiddleware((SpigotMiddleware) (executor, command) -> {
            for (Annotation annotation : command.getAnnotations()) {
                if (annotation instanceof Permission) {
                    if (!executor.getInstance().hasPermission(((Permission) annotation).value())) {
                        executor.sendMessage(Messages.format(SpigotMessages.noPermission));
                        return Middleware.Result.STOP_EXECUTION;
                    }
                }
            }
            return Middleware.Result.OK;
        });
    }

    @NotNull
    @Override
    protected SpigotCommandContainer createContainer(@NotNull Object object, @NotNull String name, @NotNull Set<String> aliases, @NotNull Map<String, WrappedCommand> commands) {
        return new SpigotCommandContainer(this, object, name, aliases, commands);
    }

    @Override
    public CommandContainer register(@NotNull Object handler, @NotNull String name, @Nullable String... aliases) throws CommandRegistrationException {
        SpigotCommandContainer container = (SpigotCommandContainer) super.register(handler, name, aliases);
        spigotReflection.register(container);
        return container;
    }

    @NotNull
    JavaPlugin getPlugin() {
        return plugin;
    }
}
