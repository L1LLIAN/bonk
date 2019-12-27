package dev.lillian.bonk.core.argument;

import dev.lillian.bonk.core.annotation.Flag;
import dev.lillian.bonk.core.command.AbstractCommandRegistry;
import dev.lillian.bonk.core.command.CommandExecution;
import dev.lillian.bonk.core.command.WrappedCommand;
import dev.lillian.bonk.core.exception.CommandArgumentException;
import dev.lillian.bonk.core.parametric.CommandParameter;
import dev.lillian.bonk.core.provider.ArgumentProvider;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class ArgumentParser {
    private final AbstractCommandRegistry<?> commandService;

    public ArgumentParser(AbstractCommandRegistry<?> commandService) {
        this.commandService = commandService;
    }

    public List<String> combineMultiWordArguments(List<String> args) {
        List<String> argList = new ArrayList<>(args.size());
        for (int i = 0; i < args.size(); i++) {
            String arg = args.get(i);
            if (!arg.isEmpty()) {
                final char c = arg.charAt(0);
                if (c == '"' || c == '\'') {
                    final StringBuilder builder = new StringBuilder();
                    int endIndex;
                    for (endIndex = i; endIndex < args.size(); endIndex++) {
                        final String arg2 = args.get(endIndex);
                        if (arg2.charAt(arg2.length() - 1) == c && arg2.length() > 1) {
                            if (endIndex != i) {
                                builder.append(' ');
                            }
                            builder.append(arg2, endIndex == i ? 1 : 0, arg2.length() - 1);
                            break;
                        } else if (endIndex == i) {
                            builder.append(arg2.substring(1));
                        } else {
                            builder.append(' ').append(arg2);
                        }
                    }
                    if (endIndex < args.size()) {
                        arg = builder.toString();
                        i = endIndex;
                    }
                }
            }
            if (!arg.isEmpty()) {
                argList.add(arg);
            }
        }
        return argList;
    }

    @NotNull
    public Object[] parseArguments(@NotNull CommandExecution execution, @NotNull WrappedCommand command, @NotNull CommandArgs args) throws IllegalArgumentException, CommandArgumentException {
        Objects.requireNonNull(command, "WrappedCommand cannot be null");
        Objects.requireNonNull(args, "CommandArgs cannot be null");
        Object[] arguments = new Object[command.getMethod().getParameterCount()];
        for (int i = 0; i < command.getParameters().getParameters().length; i++) {
            CommandParameter param = command.getParameters().getParameters()[i];
            boolean skipOptional = false; // dont complete exceptionally if true if the arg is missing
            ArgumentProvider<?> provider = command.getProviders()[i];
            String value = null;

            if (param.isFlag()) {
                Flag flag = param.getFlag();
                arguments[i] = args.getFlags().contains(flag.value());
                continue;
            } else {
                if (!args.hasNext()) {
                    if (provider.consumesArgument()) {
                        if (param.isOptional()) {
                            String defaultValue = param.getDefaultOptionalValue();
                            if (defaultValue != null && defaultValue.length() > 0) {
                                value = defaultValue;
                            } else {
                                skipOptional = true;
                            }
                        } else {
                            throw new CommandArgumentException("Missing argument for: " + provider.argumentDescription());
                        }
                    }
                }
                if (provider.consumesArgument() && value == null && args.hasNext()) {
                    value = args.next();
                }
                if (provider.consumesArgument() && value == null && !skipOptional) {
                    throw new CommandArgumentException("Argument already consumed for next argument: " + provider.argumentDescription() + " (this is a provider error!)");
                }
            }

            if (!param.isFlag()) {
                if (!skipOptional) {
                    Object o = provider.provide(new CommandArg(args.getSender(), value, args), param.getAllAnnotations());
                    o = commandService.getModifierService().executeModifiers(execution, param, o);
                    arguments[i] = o;
                } else {
                    arguments[i] = null;
                }
            }
        }
        return arguments;
    }
}
