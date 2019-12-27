package dev.lillian.bonk.core.parametric;

import dev.lillian.bonk.core.command.AbstractCommandRegistry;
import dev.lillian.bonk.core.command.WrappedCommand;
import dev.lillian.bonk.core.exception.CommandStructureException;
import dev.lillian.bonk.core.exception.MissingProviderException;
import dev.lillian.bonk.core.provider.ArgumentProvider;

public final class ProviderAssigner {
    private final AbstractCommandRegistry<?> commandService;

    public ProviderAssigner(AbstractCommandRegistry<?> commandService) {
        this.commandService = commandService;
    }

    public ArgumentProvider<?>[] assignProvidersFor(WrappedCommand wrappedCommand) throws MissingProviderException, CommandStructureException {
        CommandParameters parameters = wrappedCommand.getParameters();
        ArgumentProvider<?>[] providers = new ArgumentProvider<?>[parameters.getParameters().length];
        for (int i = 0; i < parameters.getParameters().length; i++) {
            CommandParameter param = parameters.getParameters()[i];
            if (param.isRequireLastArg() && !parameters.isLastArgument(i)) {
                throw new CommandStructureException("Parameter " + param.getParameter().getName() + " [argument " + i + "] (" + param.getParameter().getType().getSimpleName() + ") in method '" + wrappedCommand.getMethod().getName() + "' must be the last argument in the method.");
            }
            BindingContainer<?> bindings = commandService.getBindingsFor(param.getType());
            if (bindings != null) {
                ArgumentProvider<?> provider = null;
                for (CommandBinding<?> binding : bindings.getBindings()) {
                    if (binding.canProvideFor(param)) {
                        provider = binding.getProvider();
                        break;
                    }
                }
                if (provider != null) {
                    providers[i] = provider;
                } else {
                    throw new MissingProviderException("No provider bound for " + param.getType().getSimpleName() + " for parameter " + i + " for method " + wrappedCommand.getMethod().getName());
                }
            } else {
                throw new MissingProviderException("No provider bound for " + param.getType().getSimpleName());
            }
        }
        return providers;
    }
}
