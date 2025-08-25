package com.carautorox.demo.Config;

import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class JwtTokenStoreConfig {

    // Existing maps (made thread-safe)
    public static final Map<String, String> userToTokenMap = new ConcurrentHashMap<>();
    public static final Map<String, String> tokenToClientTypeMap = new ConcurrentHashMap<>();

    // Added reverse + version index
    public static final Map<String, String> tokenToUserMap = new ConcurrentHashMap<>();
    public static final Map<String, String> tokenToAllowedVersionMap = new ConcurrentHashMap<>();

    /**
     * Save or replace a token mapping.
     * @param userId         user identifier
     * @param token          JWT string
     * @param allowedVersion allowed API version (e.g., "v1", "v2")
     * @param clientType     client label (e.g., "ClientA", "SharedClient")
     */
    public static synchronized void saveToken(String userId, String token, String allowedVersion, String clientType) {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("userId is required");
        }
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("token is required");
        }

        // Remove old reverse mappings if user had a previous token
        String old = userToTokenMap.put(userId, token);
        if (old != null && !old.equals(token)) {
            tokenToUserMap.remove(old);
            tokenToClientTypeMap.remove(old);
            tokenToAllowedVersionMap.remove(old);
        }

        // Set current mappings
        tokenToUserMap.put(token, userId);

        if (clientType != null && !clientType.isBlank()) {
            tokenToClientTypeMap.put(token, clientType);
        } else {
            tokenToClientTypeMap.remove(token);
        }

        if (allowedVersion != null && !allowedVersion.isBlank()) {
            tokenToAllowedVersionMap.put(token, allowedVersion);
        } else {
            tokenToAllowedVersionMap.remove(token);
        }
    }

    // ---------- helpers (optional but useful) ----------

    /** Check if the provided token matches what we have stored for the user. */
    public static boolean isTokenValid(String userId, String token) {
        if (userId == null || token == null) return false;
        return token.equals(userToTokenMap.get(userId));
    }

    /** Get the token currently mapped to a user. */
    public static String getTokenForUser(String userId) {
        return userToTokenMap.get(userId);
    }

    /** Resolve userId from a token. */
    public static String getUserForToken(String token) {
        return tokenToUserMap.get(token);
    }

    /** Get client type label for a token. */
    public static String getClientTypeForToken(String token) {
        return tokenToClientTypeMap.get(token);
    }

    /** Get allowed API version for a token. */
    public static String getAllowedVersionForToken(String token) {
        return tokenToAllowedVersionMap.get(token);
    }

    /** Remove a user's token and related reverse mappings. */
    public static synchronized void removeToken(String userId) {
        if (userId == null) return;
        String token = userToTokenMap.remove(userId);
        if (token != null) {
            tokenToUserMap.remove(token);
            tokenToClientTypeMap.remove(token);
            tokenToAllowedVersionMap.remove(token);
        }
    }

    /** Revoke a specific token (regardless of user). */
    public static synchronized void revokeToken(String token) {
        if (token == null) return;
        String userId = tokenToUserMap.remove(token);
        if (userId != null) {
            String current = userToTokenMap.get(userId);
            if (token.equals(current)) {
                userToTokenMap.remove(userId);
            }
        }
        tokenToClientTypeMap.remove(token);
        tokenToAllowedVersionMap.remove(token);
    }

    /** True if we’ve seen this token. */
    public static boolean isKnownToken(String token) {
        return tokenToUserMap.containsKey(token);
    }

    public static void revokeByUser(String userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'revokeByUser'");
    }
}
