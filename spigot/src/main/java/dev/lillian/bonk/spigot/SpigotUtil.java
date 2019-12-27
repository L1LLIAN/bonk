package dev.lillian.bonk.spigot;

import dev.lillian.bonk.core.messages.Messages;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.CommandSender;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

final class SpigotUtil {
    private static final Method SEND_MESSAGE;
    private static final Method SPIGOT;
    private static final boolean LEGACY;

    static {
        boolean legacyTemp;
        try {
            CommandSender.class.getDeclaredMethod("spigot");
            legacyTemp = false;
        } catch (Exception ignored) {
            legacyTemp = true;
        }

        LEGACY = legacyTemp;
        Method sendMessageMethod = null;
        Method spigotMethod = null;
        if (LEGACY) {
            try {
                sendMessageMethod = CommandSender.class.getDeclaredMethod("sendMessage", BaseComponent.class);
                sendMessageMethod.setAccessible(true);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        } else {
            try {
                sendMessageMethod = CommandSender.Spigot.class.getDeclaredMethod("sendMessage", BaseComponent.class);
                spigotMethod = CommandSender.class.getDeclaredMethod("spigot");

                sendMessageMethod.setAccessible(true);
                spigotMethod.setAccessible(true);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        SEND_MESSAGE = sendMessageMethod;
        SPIGOT = spigotMethod;
    }

    private SpigotUtil() {
    }

    public static void sendMessage(CommandSender sender, BaseComponent component) {
        if (LEGACY) {
            try {
                SEND_MESSAGE.invoke(sender, component);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                sender.sendMessage(Messages.exception);
            }
        } else {
            try {
                Object spigot = SPIGOT.invoke(sender);
                SEND_MESSAGE.invoke(spigot, component);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                sender.sendMessage(Messages.exception);
            }
        }
    }
}
