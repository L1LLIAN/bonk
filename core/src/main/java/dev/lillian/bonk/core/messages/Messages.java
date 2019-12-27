package dev.lillian.bonk.core.messages;

import java.text.MessageFormat;

public final class Messages {
    /**
     * Default is an empty string, can be useful for certain platforms.
     */
    public static String prefix = "";

    public static String exception = "An exception occurred while performing this command, Please contact an administrator.";

    /**
     * {0} = attempted sub-command
     * {1} = root command
     */
    public static String unknownSubCommand = "Unknown sub-command: {0}.  Use '{1} help' to see available commands.";

    /**
     * {0} = usage
     */
    public static String usageFormat = "Command Usage: {0}";

    public static String format(String message, Object... args) {
        return new MessageFormat(prefix + message).format(args);
    }

    public static final class Providers {
        /**
         * {0} = Input
         */
        public static String invalidBoolean = "Required: Boolean (true/false), Given: ''{0}''";
        /**
         * {0} = Input
         */
        public static String invalidDouble = "Required: Decimal Number, Given: ''{0}''";
        /**
         * {0} = Input
         */
        public static String invalidInteger = "Required: Integer, Given: ''{0}''";
        /**
         * {0} = Input
         */
        public static String invalidLong = "Required: Long Number, Given: ''{0}''";
        /**
         * {0} = Input
         * {1} = Enum name
         * {2} = All enum values
         */
        public static String invalidEnumValue = "No matching value ''{0}'' found for ''{1}'', Available values: {2}";
    }
}
