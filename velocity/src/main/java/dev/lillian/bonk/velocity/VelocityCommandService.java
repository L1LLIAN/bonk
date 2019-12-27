package dev.lillian.bonk.velocity;

import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.lillian.bonk.core.annotation.Sender;
import dev.lillian.bonk.core.command.AbstractCommandRegistry;
import dev.lillian.bonk.core.command.CommandContainer;
import dev.lillian.bonk.core.command.WrappedCommand;
import dev.lillian.bonk.core.exception.CommandRegistrationException;
import dev.lillian.bonk.core.messages.Messages;
import dev.lillian.bonk.core.middleware.Middleware;
import dev.lillian.bonk.velocity.annotation.Permission;
import dev.lillian.bonk.velocity.middleware.VelocityMiddleware;
import dev.lillian.bonk.velocity.provider.CommandSourceProvider;
import dev.lillian.bonk.velocity.provider.ConsoleCommandSourceProvider;
import dev.lillian.bonk.velocity.provider.PlayerCommandSourceProvider;
import dev.lillian.bonk.velocity.provider.PlayerProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;

import static java.util.Objects.requireNonNull;

public final class VelocityCommandService extends AbstractCommandRegistry<VelocityCommandContainer> {
    private final Object plugin;
    private final ProxyServer proxyServer;
    private final CommandManager commandManager;

    public VelocityCommandService(@NotNull Object plugin, @NotNull ProxyServer proxyServer) {
        this.plugin = requireNonNull(plugin, "plugin");
        this.proxyServer = requireNonNull(proxyServer, "proxyServer");
        commandManager = proxyServer.getCommandManager();
        addDefaults();
        Messages.prefix = '\u00A7' + "c";
    }

    @Override
    protected void runAsync(@NotNull Runnable runnable) {
        proxyServer.getScheduler().buildTask(plugin, runnable).schedule();
    }

    @Override
    protected void addDefaults() {
        bind(CommandSource.class).annotatedWith(Sender.class).toProvider(new CommandSourceProvider());
        bind(ConsoleCommandSource.class).annotatedWith(Sender.class).toProvider(new ConsoleCommandSourceProvider());
        bind(Player.class).annotatedWith(Sender.class).toProvider(new PlayerCommandSourceProvider());

        bind(Player.class).toProvider(new PlayerProvider(proxyServer));

        registerMiddleware((VelocityMiddleware) (executor, command) -> {
            for (Annotation annotation : command.getAnnotations()) {
                if (annotation instanceof Permission) {
                    if (!executor.getInstance().hasPermission(((Permission) annotation).value())) {
                        executor.sendMessage(Messages.format(VelocityMessages.noPermission));
                        return Middleware.Result.STOP_EXECUTION;
                    }
                }
            }
            return Middleware.Result.OK;
        });
    }

    @Override
    protected @NotNull VelocityCommandContainer createContainer(@NotNull Object object, @NotNull String name, @NotNull Set<String> aliases, @NotNull Map<String, WrappedCommand> commands) {
        return new VelocityCommandContainer(this, object, name, aliases, commands);
    }

    @Override
    public CommandContainer register(@NotNull Object handler, @NotNull String name, @Nullable String... aliases) throws CommandRegistrationException {
        VelocityCommandContainer container = (VelocityCommandContainer) super.register(handler, name, aliases);
        commandManager.register(container.getName(), container.new VelocityCommand(this), container.getAliases().toArray(new String[0]));
        return container;
    }
}
