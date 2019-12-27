package dev.lillian.bonk.core.modifier;

import dev.lillian.bonk.core.annotation.Classifier;
import dev.lillian.bonk.core.annotation.Modifier;
import dev.lillian.bonk.core.command.CommandExecution;
import dev.lillian.bonk.core.parametric.CommandParameter;
import dev.lillian.bonk.core.util.CommandUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ModifierService {
    private final ConcurrentMap<Class<? extends Annotation>, ModifierContainer> modifiers = new ConcurrentHashMap<>();

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Nullable
    public Object executeModifiers(@NotNull CommandExecution execution, @NotNull CommandParameter param, @Nullable Object parsedArgument) throws IllegalArgumentException {
        Objects.requireNonNull(execution, "CommandExecution cannot be null");
        Objects.requireNonNull(param, "CommandParameter cannot be null");
        for (Annotation annotation : param.getModifierAnnotations()) {
            ModifierContainer container = getModifiers(annotation.annotationType());
            if (container != null) {
                for (ArgumentModifier modifier : Objects.requireNonNull(container.getModifiersFor(param.getType()))) {
                    parsedArgument = modifier.modify(execution, param, parsedArgument);
                }
            }
        }
        return parsedArgument;
    }

    public <T> void registerModifier(@NotNull Class<? extends Annotation> annotation, @NotNull Class<T> type, @NotNull ArgumentModifier<T> modifier) {
        Objects.requireNonNull(annotation, "Annotation cannot be null");
        Objects.requireNonNull(type, "Type cannot be null");
        Objects.requireNonNull(modifier, "Modifier cannot be null");
        modifiers.computeIfAbsent(annotation, a -> new ModifierContainer()).getModifiers().computeIfAbsent(type, t -> new HashSet<>()).add(modifier);
    }

    @Nullable
    public ModifierContainer getModifiers(@NotNull Class<? extends Annotation> annotation) {
        Objects.requireNonNull(annotation, "Annotation cannot be null");
        CommandUtil.checkState(isModifier(annotation), "Annotation provided is not a modifier (annotate with @Modifier) for getModifier: " + annotation.getSimpleName());
        CommandUtil.checkState(!isClassifier(annotation), "Annotation provided cannot be an @Classifier and an @Modifier: " + annotation.getSimpleName());
        if (modifiers.containsKey(annotation)) {
            return modifiers.get(annotation);
        }
        return null;
    }

    public boolean isModifier(@NotNull Class<? extends Annotation> type) {
        return type.isAnnotationPresent(Modifier.class);
    }

    public boolean isClassifier(@NotNull Class<? extends Annotation> type) {
        return type.isAnnotationPresent(Classifier.class);
    }
}
