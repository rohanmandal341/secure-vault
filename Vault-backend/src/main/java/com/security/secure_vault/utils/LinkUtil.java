package com.security.secure_vault.utils;

import java.util.UUID;

public class LinkUtil {
    public static String generateToken() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 12);
    }
}
