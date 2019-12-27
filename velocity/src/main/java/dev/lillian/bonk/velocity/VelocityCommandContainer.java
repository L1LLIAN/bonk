package dev.lillian.bonk.velocity;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import dev.lillian.bonk.core.command.AbstractCommandRegistry;
import dev.lillian.bonk.core.command.CommandContainer;
import dev.lillian.bonk.core.command.WrappedCommand;
import dev.lillian.bonk.core.executor.CommandExecutor;
import dev.lillian.bonk.velocity.annotation.Permission;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.Set;

final class VelocityCommandContainer extends CommandContainer {
    public VelocityCommandContainer(AbstractCommandRegistry<?> commandService, Object object, String name, Set<String> aliases, Map<String, WrappedCommand> commands) {
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

    public final class VelocityCommand implements SimpleCommand {
        private final VelocityCommandService commandService;

        public VelocityCommand(VelocityCommandService commandService) {
            this.commandService = commandService;
        }

        @Override
        public void execute(Invocation invocation) {
            CommandExecutor<CommandSource> executor = new VelocityCommandExecutor(invocation.source());
            commandService.executeCommand(executor, VelocityCommandContainer.this, invocation.alias(), invocation.arguments());
        }

        @Override
        public List<String> suggest(Invocation invocation) {
            CommandExecutor<CommandSource> executor = new VelocityCommandExecutor(invocation.source());
            return tabCompleter.onTabComplete(executor, VelocityCommandContainer.this.getName(), invocation.arguments());
        }

        @Override
        public boolean hasPermission(Invocation invocation) {
            return invocation.source().hasPermission(getPermission());
        }
    }
}
