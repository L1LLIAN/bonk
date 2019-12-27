package dev.lillian.bonk.core.provider.impl;

import dev.lillian.bonk.core.argument.CommandArg;
import dev.lillian.bonk.core.executor.CommandExecutor;
import dev.lillian.bonk.core.messages.Messages;
import dev.lillian.bonk.core.provider.ArgumentProvider;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;

public final class LongProvider implements ArgumentProvider<Long> {
    @Override
    public Long provide(@NotNull CommandArg arg, @NotNull List<? extends Annotation> annotations) throws IllegalArgumentException {
        String s = arg.get();
        try {
            return Long.parseLong(s);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(Messages.format(Messages.Providers.invalidLong, s));
        }
    }

    @NotNull
    @Override
    public String argumentDescription() {
        return "number";
    }

    @NotNull
    @Override
    public List<String> provideSuggestions(@NotNull CommandExecutor<?> executor, @NotNull String input) {
        return Collections.emptyList();
    }
}
