package org.humanized.tools.model;

import java.util.UUID;

public class GUID {

    public static boolean isValid(String guid) {
        boolean valid = false;
        if (guid != null) {
            try {
                UUID.fromString(guid);
                valid = true;
            } catch (IllegalArgumentException ignore) {
                // Ignore
            }
        }
        return valid;
    }


    public static String generate() {
        return UUID.randomUUID().toString().toUpperCase();
    }
}
