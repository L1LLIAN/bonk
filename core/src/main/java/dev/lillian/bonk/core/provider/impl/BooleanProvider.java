package dev.lillian.bonk.core.provider.impl;

import dev.lillian.bonk.core.argument.CommandArg;
import dev.lillian.bonk.core.executor.CommandExecutor;
import dev.lillian.bonk.core.messages.Messages;
import dev.lillian.bonk.core.provider.ArgumentProvider;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class BooleanProvider implements ArgumentProvider<Boolean> {
    private static final List<String> SUGGEST = Arrays.asList("true", "false");
    private static final List<String> SUGGEST_TRUE = Collections.singletonList("true");
    private static final List<String> SUGGEST_FALSE = Collections.singletonList("false");

    @Override
    public Boolean provide(@NotNull CommandArg arg, @NotNull List<? extends Annotation> annotations) throws IllegalArgumentException {
        String s = arg.get();
        if (s == null) {
            return false;
        }
        try {
            return Boolean.parseBoolean(s);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(Messages.format(Messages.Providers.invalidBoolean, s));
        }
    }

    @NotNull
    @Override
    public String argumentDescription() {
        return "true/false";
    }

    @NotNull
    @Override
    public List<String> provideSuggestions(@NotNull CommandExecutor<?> executor, @NotNull String input) {
        input = input.toLowerCase();
        if (input.length() == 0) {
            return SUGGEST;
        }
        if ("true".startsWith(input)) {
            return SUGGEST_TRUE;
        } else if ("false".startsWith(input)) {
            return SUGGEST_FALSE;
        } else {
            return Collections.emptyList();
        }
    }
}
