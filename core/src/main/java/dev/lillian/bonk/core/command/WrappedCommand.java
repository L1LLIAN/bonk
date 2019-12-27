package dev.lillian.bonk.core.command;

import dev.lillian.bonk.core.exception.CommandStructureException;
import dev.lillian.bonk.core.exception.MissingProviderException;
import dev.lillian.bonk.core.parametric.CommandParameter;
import dev.lillian.bonk.core.parametric.CommandParameters;
import dev.lillian.bonk.core.provider.ArgumentProvider;
import dev.lillian.bonk.core.provider.SuggestionProvider;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

@Getter
public final class WrappedCommand {
    private final AbstractCommandRegistry<?> commandService;
    private final String name;
    private final Set<String> allAliases;
    private final Set<String> aliases;
    private final String description;
    private final String usage;
    private final Object handler;
    private final Method method;
    private final boolean async;
    private final CommandParameters parameters;
    private final ArgumentProvider<?>[] providers;
    private final ArgumentProvider<?>[] consumingProviders;
    private final SuggestionProvider[] suggestionProviders;
    private final int consumingArgCount;
    private final int requiredArgCount;
    private final String generatedUsage;
    private final Annotation[] annotations;
    private final Set<String> flags = new HashSet<>();

    public WrappedCommand(AbstractCommandRegistry<?> commandService, String name, Set<String> aliases, String description, String usage, Object handler, Method method, boolean async, Annotation[] annotations) throws MissingProviderException, CommandStructureException {
        this.commandService = commandService;
        this.name = name;
        this.aliases = aliases;
        this.description = description;
        this.usage = usage;
        this.annotations = annotations;
        this.handler = handler;
        this.method = method;
        this.async = async;
        this.parameters = new CommandParameters(commandService, method);
        for (CommandParameter parameter : parameters.getParameters()) {
            if (parameter.isFlag()) {
                flags.add(parameter.getFlag().value());
            }
        }
        this.providers = commandService.getProviderAssigner().assignProvidersFor(this);
        this.consumingArgCount = calculateConsumingArgCount();
        this.requiredArgCount = calculateRequiredArgCount();
        this.consumingProviders = calculateConsumingProviders();
        this.suggestionProviders = calculateSuggestionProviders();
        this.generatedUsage = generateUsage();
        this.allAliases = aliases;
        if (name.length() > 0 && !name.equals(AbstractCommandRegistry.DEFAULT_KEY)) {
            allAliases.add(name);
        }
    }

    public String getMostApplicableUsage() {
        if (usage.length() > 0) {
            return usage;
        } else {
            return generatedUsage;
        }
    }

    public String getShortDescription() {
        if (description.length() > 24) {
            return description.substring(0, 21) + "...";
        } else {
            return description;
        }
    }

    private String generateUsage() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parameters.getParameters().length; i++) {
            CommandParameter parameter = parameters.getParameters()[i];
            ArgumentProvider<?> provider = providers[i];
            if (parameter.isFlag()) {
                sb.append("[-");
                sb.append(parameter.getFlag().value());
                if (parameter.getDescription() != null) {
                    sb.append(" (");
                    sb.append(parameter.getDescription());
                    sb.append(")");
                }
                sb.append("] ");
            } else {
                String description = parameter.getDescription() == null ? provider.argumentDescription() : parameter.getDescription();
                if (provider.consumesArgument()) {
                    if (parameter.isOptional()) {
                        sb.append("[").append(description);
                        if (parameter.isText()) {
                            sb.append("...");
                        }
                        if (parameter.getDefaultOptionalValue() != null && parameter.getDefaultOptionalValue().length() > 0) {
                            sb.append(" = ").append(parameter.getDefaultOptionalValue());
                        }
                        sb.append("]");
                    } else {
                        sb.append("<").append(description);
                        if (parameter.isText()) {
                            sb.append("...");
                        }
                        sb.append(">");
                    }
                    sb.append(" ");
                }
            }
        }
        return sb.toString();
    }

    private ArgumentProvider<?>[] calculateConsumingProviders() {
        ArgumentProvider<?>[] consumingProviders = new ArgumentProvider<?>[consumingArgCount];
        int x = 0;
        for (int i = 0; i < providers.length; i++) {
            ArgumentProvider<?> provider = providers[i];
            CommandParameter parameter = parameters.getParameters()[i];
            if (provider.consumesArgument() && !parameter.isFlag()) {
                consumingProviders[x] = provider;
                x++;
            }
        }
        return consumingProviders;
    }

    private int calculateConsumingArgCount() {
        int count = 0;
        for (int i = 0; i < providers.length; i++) {
            ArgumentProvider<?> provider = providers[i];
            if (provider.consumesArgument() && !parameters.getParameters()[i].isFlag()) {
                count++;
            }
        }
        return count;
    }

    private SuggestionProvider[] calculateSuggestionProviders() {
        SuggestionProvider[] suggestionProviders = new SuggestionProvider[consumingArgCount];
        int x = 0;
        for (int i = 0; i < providers.length; i++) {
            CommandParameter parameter = parameters.getParameters()[i];
            if (!providers[i].consumesArgument() || parameter.isFlag()) {
                continue;
            }

            if (parameter.getSuggestionProvider() == null) {
                suggestionProviders[x] = consumingProviders[x];
            } else {
                suggestionProviders[x] = parameter.getSuggestionProvider();
            }
            x++;
        }
        return suggestionProviders;
    }

    private int calculateRequiredArgCount() {
        int count = 0;
        for (int i = 0; i < parameters.getParameters().length; i++) {
            CommandParameter parameter = parameters.getParameters()[i];
            if (!parameter.isFlag() && !parameter.isOptional()) {
                ArgumentProvider<?> provider = providers[i];
                if (provider.consumesArgument()) {
                    count++;
                }
            }
        }
        return count;
    }

    @NotNull
    public Set<String> getFlags() {
        return flags;
    }
}
