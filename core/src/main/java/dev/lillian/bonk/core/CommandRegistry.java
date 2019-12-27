package dev.lillian.bonk.core;

import dev.lillian.bonk.core.annotation.Command;
import dev.lillian.bonk.core.annotation.Modifier;
import dev.lillian.bonk.core.command.CommandContainer;
import dev.lillian.bonk.core.help.HelpFormatter;
import dev.lillian.bonk.core.middleware.Middleware;
import dev.lillian.bonk.core.modifier.ArgumentModifier;
import dev.lillian.bonk.core.parametric.binder.CommandBinder;
import dev.lillian.bonk.core.provider.ArgumentProvider;
import dev.lillian.bonk.core.provider.SuggestionProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;

public interface CommandRegistry {
    /**
     * Register a command into the Command Service
     *
     * @param handler Object that has the {@link Command} annotated methods
     * @param name    The name of the command to register.
     *                The names of methods within the handler object will be sub-commands to this name.
     *                If you want to create a default command (just name), set the name here and in the
     *                {@link Command} annotation set name = ""
     * @param aliases (Optional) A list of alternate command names that can be used
     * @return The {@link CommandContainer} containing the command you registered
     */
    CommandContainer register(@NotNull Object handler, @NotNull String name, @Nullable String... aliases);

    /**
     * Start binding a class type to a {@link ArgumentProvider} or instance.
     *
     * @param type The Class type to bind to
     * @param <T>  The type of class
     * @return A {@link CommandBinder} instance to finish the binding
     */
    <T> CommandBinder<T> bind(@NotNull Class<T> type);

    /**
     * Registers a modifier to modify provided arguments for a specific type
     *
     * @param annotation The annotation to use for the modifier (must have {@link Modifier} annotated in it's class)
     * @param type       The type to modify
     * @param modifier   The modifier
     * @param <T>        The type of class to modify
     */
    <T> void registerModifier(@NotNull Class<? extends Annotation> annotation, @NotNull Class<T> type, @NotNull ArgumentModifier<T> modifier);

    /**
     * Registers middleware which executes before *every* command is ran
     *
     * @param middleware The middleware to register
     */
    void registerMiddleware(@NotNull Middleware<?> middleware);

    /**
     * Registers a suggestion provider by name that can be used
     * by arguments annotated with {@link dev.lillian.bonk.core.annotation.Suggest}
     *
     * @param name               The name of the suggestion provider
     * @param suggestionProvider The suggestion provider to register
     */
    void registerSuggestionProvider(@NotNull String name, @NotNull SuggestionProvider suggestionProvider);

    /**
     * @return A suggestion provider by name if found, else null
     */
    @Nullable
    SuggestionProvider getSuggestionProvider(@NotNull String name);

    /**
     * Set the help formatter
     *
     * @param helpFormatter The new {@link HelpFormatter} instance
     */
    void setHelpFormatter(@NotNull HelpFormatter helpFormatter);
}
