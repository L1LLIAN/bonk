package dev.lillian.bonk.core.command;

import dev.lillian.bonk.core.annotation.Command;
import dev.lillian.bonk.core.exception.CommandRegistrationException;
import dev.lillian.bonk.core.exception.CommandStructureException;
import dev.lillian.bonk.core.exception.MissingProviderException;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.*;

public final class CommandExtractor {
    private final AbstractCommandRegistry<?> commandService;

    public CommandExtractor(AbstractCommandRegistry<?> commandService) {
        this.commandService = commandService;
    }

    public Map<String, WrappedCommand> extractCommands(@NotNull Object handler) throws MissingProviderException, CommandStructureException {
        Objects.requireNonNull(handler, "Handler object cannot be null");
        final Map<String, WrappedCommand> commands = new HashMap<>();
        for (Method method : handler.getClass().getDeclaredMethods()) {
            Optional<WrappedCommand> o = extractCommand(handler, method);
            if (o.isPresent()) {
                WrappedCommand wrappedCommand = o.get();
                commands.put(commandService.getCommandKey(wrappedCommand.getName()), wrappedCommand);
            }
        }
        return commands;
    }

    private Optional<WrappedCommand> extractCommand(@NotNull Object handler, @NotNull Method method) throws MissingProviderException, CommandStructureException {
        Objects.requireNonNull(handler, "Handler object cannot be null");
        Objects.requireNonNull(method, "Method cannot be null");
        if (method.isAnnotationPresent(Command.class)) {
            try {
                method.setAccessible(true);
            } catch (SecurityException ex) {
                throw new CommandRegistrationException("Couldn't access method " + method.getName());
            }
            Command command = method.getAnnotation(Command.class);
            WrappedCommand wrappedCommand = new WrappedCommand(
                    commandService, command.name(), new HashSet<>(Arrays.asList(command.aliases())),
                    command.desc(), command.usage(), handler, method, command.async(), method.getDeclaredAnnotations()
            );
            return Optional.of(wrappedCommand);
        }
        return Optional.empty();
    }
}
