package dev.lillian.bonk.core.parametric;

import dev.lillian.bonk.core.CommandRegistry;
import dev.lillian.bonk.core.annotation.*;
import dev.lillian.bonk.core.exception.CommandRegistrationException;
import dev.lillian.bonk.core.provider.SuggestionProvider;
import lombok.Getter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
public class CommandParameter {
    private final Class<?> type;
    private final Parameter parameter;
    private final List<Annotation> allAnnotations;
    private final List<Annotation> classifierAnnotations;
    private final List<Annotation> modifierAnnotations;
    private final boolean flag;
    private final boolean requireLastArg;
    private final String description;
    private final SuggestionProvider suggestionProvider;

    public CommandParameter(CommandRegistry service, Class<?> type, Parameter parameter, Annotation[] allAnnotations) {
        this.type = type;
        this.parameter = parameter;
        this.allAnnotations = Arrays.asList(allAnnotations);
        this.classifierAnnotations = loadClassifiers();
        this.modifierAnnotations = loadModifiers();
        this.flag = loadFlag();
        this.requireLastArg = calculateRequireLastArg();
        this.description = loadDescription();

        SuggestionProvider suggestionProvider = null;
        for (Annotation annotation : allAnnotations) {
            if (annotation instanceof Suggest) {
                suggestionProvider = service.getSuggestionProvider(((Suggest) annotation).value());
            }
        }

        this.suggestionProvider = suggestionProvider;

        if (flag && !(parameter.getType().equals(boolean.class) || parameter.getType().equals(Boolean.class))) {
            throw new CommandRegistrationException("Cannot use non boolean type for a flag.");
        }
    }

    private boolean calculateRequireLastArg() {
        for (Annotation annotation : allAnnotations) {
            if (annotation.annotationType().isAnnotationPresent(LastArg.class)) {
                return true;
            }
        }
        return false;
    }

    public boolean isText() {
        return parameter.isAnnotationPresent(Text.class);
    }

    public boolean isOptional() {
        return parameter.isAnnotationPresent(OptArg.class);
    }

    public String getDefaultOptionalValue() {
        return parameter.getAnnotation(OptArg.class).value();
    }

    private boolean loadFlag() {
        return parameter.isAnnotationPresent(Flag.class);
    }

    public boolean isFlag() {
        return flag;
    }

    public Flag getFlag() {
        return parameter.getAnnotation(Flag.class);
    }

    private List<Annotation> loadClassifiers() {
        List<Annotation> classifiers = new ArrayList<>();
        for (Annotation annotation : allAnnotations) {
            if (annotation.annotationType().isAnnotationPresent(Classifier.class)) {
                classifiers.add(annotation);
            }
        }
        return Collections.unmodifiableList(classifiers);
    }

    private List<Annotation> loadModifiers() {
        List<Annotation> modifiers = new ArrayList<>();
        for (Annotation annotation : allAnnotations) {
            if (annotation.annotationType().isAnnotationPresent(Modifier.class)) {
                modifiers.add(annotation);
            }
        }
        return Collections.unmodifiableList(modifiers);
    }

    private String loadDescription() {
        for (Annotation annotation : allAnnotations) {
            if (annotation instanceof Desc) {
                return ((Desc) annotation).value();
            }
        }

        return null;
    }
}
