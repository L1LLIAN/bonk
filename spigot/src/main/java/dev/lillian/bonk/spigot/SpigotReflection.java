package dev.lillian.bonk.spigot;

import dev.lillian.bonk.core.exception.CommandRegistrationException;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

final class SpigotReflection {
    private final SpigotCommandRegistry registry;
    private CommandMap commandMap;

    SpigotReflection(SpigotCommandRegistry registry) {
        this.registry = registry;
        try {
            commandMap = (CommandMap) getPrivateField(Bukkit.getServer(), "commandMap", false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void register(@NotNull SpigotCommandContainer container) throws CommandRegistrationException {
        try {
            Map<String, Command> knownCommands = getKnownCommands();
            if (knownCommands.containsKey(container.getName().toLowerCase())) {
                knownCommands.remove(container.getName().toLowerCase()).unregister(commandMap);
            }
            for (String s : container.getAliases()) {
                if (knownCommands.containsKey(s.toLowerCase())) {
                    knownCommands.remove(s).unregister(commandMap);
                }
            }
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new CommandRegistrationException("Couldn't access knownCommands field in Bukkit CommandMap to unregister existing command(s)");
        }

        commandMap.register(registry.getPlugin().getName(), container.new SpigotCommand(registry));
    }

    private Map<String, Command> getKnownCommands() throws NoSuchFieldException, IllegalAccessException {
        Object map = getPrivateField(commandMap, "knownCommands", true);
        @SuppressWarnings("unchecked")
        HashMap<String, Command> knownCommands = (HashMap<String, Command>) map;
        return knownCommands;
    }

    private Object getPrivateField(Object object, String field, boolean fallback) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Class<?> clazz = object.getClass();
        Field objectField;

        try {
            objectField = clazz.getDeclaredField(field);
        } catch (NoSuchFieldException e) {
            if (fallback) {
                objectField = clazz.getSuperclass().getDeclaredField(field);
            } else {
                throw new NoSuchFieldException(e.getMessage());
            }
        }

        objectField.setAccessible(true);
        Object result = objectField.get(object);
        objectField.setAccessible(false);
        return result;
    }
}
