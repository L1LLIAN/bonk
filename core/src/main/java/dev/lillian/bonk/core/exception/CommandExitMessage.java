package dev.lillian.bonk.core.exception;

import lombok.Getter;

@Getter
public final class CommandExitMessage extends Exception {
    private final String message;
    private final boolean showUsage;

    public CommandExitMessage(String message, boolean showUsage) {
        super(message);
        this.message = message;
        this.showUsage = showUsage;
    }

    public CommandExitMessage(String message) {
        this(message, true);
    }
}
