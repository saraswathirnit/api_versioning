package com.carautorox.demo.Authentication;

import java.util.HashMap;
import java.util.Map;

public class UserAuthStore {

    private static final Map<String, String> userToSecurityCodeMap = new HashMap<>();
    private static final Map<String, String> userToVersionMap = new HashMap<>();

    static {
        // Initialize credentials here for simplicity (static block)
        userToSecurityCodeMap.put("Axcoo1", "XAXmz123Encrypted");
        userToVersionMap.put("Axcoo1", "v1");

        userToSecurityCodeMap.put("Bxc002", "BXCmz456Encrypted");
        userToVersionMap.put("Bxc002", "v2");

        userToSecurityCodeMap.put("SharedUser", "SHAREDkeyEncrypted");
        userToVersionMap.put("SharedUser", "both");
    }

    public static boolean isValidUser(String userId, String securityCode) {
        if (userId == null || securityCode == null) return false;
        String u = userId.trim();
        String s = securityCode.trim();
        if (!userToSecurityCodeMap.containsKey(u)) return false;
        String stored = userToSecurityCodeMap.get(u);
        return stored != null && stored.trim().equals(s);
    }

    public static String getAllowedVersion(String userId) {
        if (userId == null) return null;
        return userToVersionMap.get(userId.trim());
    }
}
