package dev.lillian.bonk.core.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CommandUtil {
    public String join(String[] strings, char separator) {
        if (strings == null) {
            return null;
        }
        if (strings.length <= 0) {
            return "";
        }

        StringBuilder builder = new StringBuilder(strings.length);
        // check first arg, don't add separator here just incase its the only argument
        if (strings[0] != null) {
            builder.append(strings[0]);
        }

        // already added first arg
        for (int i = 1; i < strings.length; i++) {
            builder.append(separator);
            // Apache keeps separator even for null value
            if (strings[i] != null) {
                builder.append(strings[i]);
            }
        }

        return builder.toString();
    }

    public void checkState(boolean state, String error) {
        if (!state) {
            throw new IllegalStateException(error);
        }
    }
}
