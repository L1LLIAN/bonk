package dev.lillian.bonk.core.provider.impl;

import dev.lillian.bonk.core.argument.CommandArg;
import dev.lillian.bonk.core.executor.CommandExecutor;
import dev.lillian.bonk.core.provider.ArgumentProvider;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;

public final class GreedyStringProvider implements ArgumentProvider<String> {
    @Override
    public String provide(@NotNull CommandArg arg, @NotNull List<? extends Annotation> annotations) {
        StringBuilder builder = new StringBuilder(arg.get());
        while (arg.getArgs().hasNext()) {
            builder.append(" ").append(arg.getArgs().next());
        }
        return builder.toString();
    }

    @NotNull
    @Override
    public String argumentDescription() {
        return "text";
    }

    @NotNull
    @Override
    public List<String> provideSuggestions(@NotNull CommandExecutor<?> executor, @NotNull String input) {
        return Collections.emptyList();
    }
}
