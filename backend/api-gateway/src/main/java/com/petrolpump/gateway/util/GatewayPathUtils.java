package com.petrolpump.gateway.util;

/**
 * Normalizes request paths before security checks (trailing slashes, duplicate slashes).
 */
public final class GatewayPathUtils {

    private GatewayPathUtils() {
    }

    public static String normalize(String path) {
        if (path == null || path.isBlank()) {
            return "/";
        }
        String normalized = path.trim();
        if (!normalized.startsWith("/")) {
            normalized = "/" + normalized;
        }
        while (normalized.length() > 1 && normalized.endsWith("/")) {
            normalized = normalized.substring(0, normalized.length() - 1);
        }
        return normalized;
    }
}
