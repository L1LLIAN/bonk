package dev.lillian.bonk.core.provider.impl;

import dev.lillian.bonk.core.argument.CommandArg;
import dev.lillian.bonk.core.argument.CommandArgs;
import dev.lillian.bonk.core.executor.CommandExecutor;
import dev.lillian.bonk.core.provider.ArgumentProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;

public final class CommandArgsProvider implements ArgumentProvider<CommandArgs> {
    @Override
    public boolean consumesArgument() {
        return false;
    }

    @Nullable
    @Override
    public CommandArgs provide(@NotNull CommandArg arg, @NotNull List<? extends Annotation> annotations) {
        return arg.getArgs();
    }

    @NotNull
    @Override
    public String argumentDescription() {
        return "args";
    }

    @NotNull
    @Override
    public List<String> provideSuggestions(@NotNull CommandExecutor<?> executor, @NotNull String input) {
        return Collections.emptyList();
    }
}
