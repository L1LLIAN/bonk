package dev.lillian.bonk.core.flag;

import dev.lillian.bonk.core.command.WrappedCommand;
import dev.lillian.bonk.core.exception.CommandArgumentException;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class FlagExtractor {
    @NotNull
    public static Set<String> extractProvidedFlags(@NotNull WrappedCommand command, @NotNull List<String> args) throws CommandArgumentException {
        Objects.requireNonNull(args, "Args cannot be null");
        Set<String> flags = new HashSet<>();
        Iterator<String> iterator = args.iterator();
        while (iterator.hasNext()) {
            String arg = iterator.next();
            if (arg.charAt(0) == '-' && command.getFlags().contains(arg.substring(1))) {
                flags.add(arg.substring(1));
                iterator.remove();
            }
        }
        return flags;
    }
}
