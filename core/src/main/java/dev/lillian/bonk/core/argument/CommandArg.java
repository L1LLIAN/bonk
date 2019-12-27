package dev.lillian.bonk.core.argument;

import dev.lillian.bonk.core.executor.CommandExecutor;

public final class CommandArg {
    private final CommandExecutor<?> sender;
    private final String value;
    private final String label;
    private final CommandArgs args;

    public CommandArg(CommandExecutor<?> sender, String value, CommandArgs args) {
        this.sender = sender;
        this.value = value;
        this.label = args.getLabel();
        this.args = args;
    }

    public CommandExecutor<?> getSender() {
        return sender;
    }

    public String get() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    public CommandArgs getArgs() {
        return args;
    }
}
