package dev.lillian.bonk.core.argument;

import dev.lillian.bonk.core.command.AbstractCommandRegistry;
import dev.lillian.bonk.core.executor.CommandExecutor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

@Getter
public final class CommandArgs {
    private final AbstractCommandRegistry<?> commandService;
    private final CommandExecutor<?> sender;
    private final List<String> args;
    private final String label;
    private final Set<String> flags;
    private final ReentrantLock lock = new ReentrantLock();
    private int index = 0;

    public CommandArgs(@NotNull AbstractCommandRegistry<?> commandService, @NotNull CommandExecutor<?> sender, @NotNull String label, @NotNull List<String> args,
                       @NotNull Set<String> flags) {
        Objects.requireNonNull(commandService, "CommandService cannot be null");
        Objects.requireNonNull(sender, "CommandSender cannot be null");
        Objects.requireNonNull(label, "Label cannot be null");
        Objects.requireNonNull(args, "Command args cannot be null");
        this.commandService = commandService;
        this.sender = sender;
        this.label = label;
        this.args = new ArrayList<>(args);
        this.flags = flags;
    }

    public boolean hasNext() {
        lock.lock();
        try {
            return args.size() > index;
        } finally {
            lock.unlock();
        }
    }

    public String next() {
        lock.lock();
        try {
            return args.get(index++);
        } finally {
            lock.unlock();
        }
    }
}
