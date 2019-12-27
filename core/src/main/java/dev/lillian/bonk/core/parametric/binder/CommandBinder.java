package dev.lillian.bonk.core.parametric.binder;

import dev.lillian.bonk.core.command.AbstractCommandRegistry;
import dev.lillian.bonk.core.provider.ArgumentProvider;
import dev.lillian.bonk.core.provider.impl.InstanceProvider;
import dev.lillian.bonk.core.util.CommandUtil;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class CommandBinder<T> {
    private final AbstractCommandRegistry<?> commandService;
    private final Class<T> type;
    private final Set<Class<? extends Annotation>> classifiers = new HashSet<>();
    private ArgumentProvider<T> provider;

    public CommandBinder(AbstractCommandRegistry<?> commandService, Class<T> type) {
        this.commandService = commandService;
        this.type = type;
    }

    public CommandBinder<T> annotatedWith(@NotNull Class<? extends Annotation> annotation) {
        CommandUtil.checkState(commandService.getModifierService().isClassifier(annotation), "Annotation " + annotation.getSimpleName() + " must have @Classifer to be bound");
        classifiers.add(annotation);
        return this;
    }

    public void toInstance(@NotNull T instance) {
        Objects.requireNonNull(instance, "Instance cannot be null for toInstance during binding for " + type.getSimpleName());
        this.provider = new InstanceProvider<>(instance);
        finish();
    }

    public void toProvider(@NotNull ArgumentProvider<T> provider) {
        Objects.requireNonNull(provider, "Provider cannot be null for toProvider during binding for " + type.getSimpleName());
        this.provider = provider;
        finish();
    }

    private void finish() {
        commandService.bindProvider(type, classifiers, provider);
    }
}
