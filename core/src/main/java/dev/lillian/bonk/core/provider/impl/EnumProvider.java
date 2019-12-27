package dev.lillian.bonk.core.provider.impl;

import dev.lillian.bonk.core.argument.CommandArg;
import dev.lillian.bonk.core.executor.CommandExecutor;
import dev.lillian.bonk.core.messages.Messages;
import dev.lillian.bonk.core.provider.ArgumentProvider;
import dev.lillian.bonk.core.util.CommandUtil;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public final class EnumProvider<T extends Enum<T>> implements ArgumentProvider<T> {
    private static final Pattern NON_ALPHANUMERIC = Pattern.compile("[^A-Za-z0-9]");

    private final Class<T> enumClass;

    public EnumProvider(@NotNull Class<T> enumClass) {
        this.enumClass = enumClass;
    }

    private static String simplify(String t) {
        return NON_ALPHANUMERIC.matcher(t.toLowerCase()).replaceAll("");
    }

    @Override
    public T provide(@NotNull CommandArg arg, @NotNull List<? extends Annotation> annotations) throws IllegalArgumentException {
        String name = arg.get();
        String s = simplify(name);

        for (T entry : enumClass.getEnumConstants()) {
            if (simplify(entry.name()).equalsIgnoreCase(s)) {
                return entry;
            }
        }
        throw new IllegalArgumentException(Messages.format(Messages.Providers.invalidEnumValue, s, argumentDescription(), CommandUtil.join(provideSuggestions(arg.getSender(), "").toArray(new String[0]), ' ')));
    }

    @NotNull
    @Override
    public String argumentDescription() {
        return enumClass.getSimpleName();
    }

    @NotNull
    @Override
    public List<String> provideSuggestions(@NotNull CommandExecutor<?> executor, @NotNull String input) {
        List<String> suggestions = new ArrayList<>();
        String test = simplify(input);

        for (T entry : enumClass.getEnumConstants()) {
            String name = simplify(entry.name());
            if (test.length() == 0 || name.startsWith(test)) {
                suggestions.add(entry.name().toLowerCase());
            }
        }

        return suggestions;
    }
}
