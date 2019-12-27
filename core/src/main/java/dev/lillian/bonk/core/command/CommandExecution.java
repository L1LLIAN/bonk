package dev.lillian.bonk.core.command;

import dev.lillian.bonk.core.argument.CommandArgs;
import dev.lillian.bonk.core.executor.CommandExecutor;
import lombok.Getter;

import java.util.List;

@Getter
public final class CommandExecution {
    private final AbstractCommandRegistry<?> commandService;
    private final CommandExecutor<?> sender;
    private final List<String> args;
    private final CommandArgs commandArgs;
    private final WrappedCommand command;
    private boolean canExecute = true;

    public CommandExecution(AbstractCommandRegistry<?> commandService, CommandExecutor<?> sender, List<String> args, CommandArgs commandArgs, WrappedCommand command) {
        this.commandService = commandService;
        this.sender = sender;
        this.args = args;
        this.commandArgs = commandArgs;
        this.command = command;
    }

    public void preventExecution() {
        canExecute = false;
    }
}
