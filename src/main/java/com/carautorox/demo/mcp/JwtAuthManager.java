package com.carautorox.demo.mcp;

import com.carautorox.demo.Authentication.jwt.JwtUtil;
import com.carautorox.demo.Config.JwtTokenStoreConfig;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;

@Component
public class JwtAuthManager implements McpToolHandler {

    private final JwtUtil jwtUtil;

    public JwtAuthManager(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public String getToolName() {
        return "JwtAuthManager";
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object execute(Object input) {
        Map<String, Object> args = coerceToMap(input);
        String op = String.valueOf(args.get("op")).toLowerCase(Locale.ROOT);

        return switch (op) {
            case "validate" -> ok(handleValidate(args));
            case "save" -> ok(handleSave(args));
            case "revokebyuser" -> ok(handleRevokeByUser(args));
            case "revokebytoken" -> ok(handleRevokeByToken(args));
            case "whois" -> ok(handleWhois(args));
            default -> error("UNKNOWN_OP", "Unsupported op: " + op);
        };
    }

    private Map<String, Object> handleValidate(Map<String, Object> args) {
        String userId = reqStr(args, "userId");
        String token = reqStr(args, "token");
        boolean jwtValid = jwtUtil.validateToken(token);
        String mapped = JwtTokenStoreConfig.getUserForToken(token);
        boolean known = mapped != null;
        boolean matches = userId.equals(mapped);
        Map<String, Object> res = new LinkedHashMap<>();
        res.put("jwtValid", jwtValid);
        res.put("knownToken", known);
        res.put("userMatches", matches);
        res.put("authenticated", jwtValid && known && matches);
        res.put("clientType", JwtTokenStoreConfig.getClientTypeForToken(token));
        res.put("allowedVersion", JwtTokenStoreConfig.getAllowedVersionForToken(token));
        return res;
    }

    private Map<String, Object> handleSave(Map<String, Object> args) {
        String userId = reqStr(args, "userId");
        String token = reqStr(args, "token");
        String allowedVersion = optStr(args, "allowedVersion");
        String clientType = optStr(args, "clientType");
        JwtTokenStoreConfig.saveToken(userId, token, allowedVersion, clientType);
        return Map.of("saved", true, "userId", userId,
                "clientType", JwtTokenStoreConfig.getClientTypeForToken(token),
                "allowedVersion", JwtTokenStoreConfig.getAllowedVersionForToken(token));
    }

    private Map<String, Object> handleRevokeByUser(Map<String, Object> args) {
        String userId = reqStr(args, "userId");
        String prev = JwtTokenStoreConfig.getTokenForUser(userId);
        if (prev == null) throw new NoSuchElementException("No token for userId=" + userId);
        JwtTokenStoreConfig.revokeByUser(userId);
        return Map.of("revoked", true, "userId", userId, "token", prev);
    }

    private Map<String, Object> handleRevokeByToken(Map<String, Object> args) {
        String token = reqStr(args, "token");
        if (!JwtTokenStoreConfig.isKnownToken(token)) throw new NoSuchElementException("Unknown token");
        String userId = JwtTokenStoreConfig.getUserForToken(token);
        JwtTokenStoreConfig.revokeToken(token);
        return Map.of("revoked", true, "userId", userId, "token", token);
    }

    private Map<String, Object> handleWhois(Map<String, Object> args) {
        String token = reqStr(args, "token");
        String userId = JwtTokenStoreConfig.getUserForToken(token);
        if (userId == null) throw new NoSuchElementException("Unknown token");
        return Map.of("userId", userId,
                "clientType", JwtTokenStoreConfig.getClientTypeForToken(token),
                "allowedVersion", JwtTokenStoreConfig.getAllowedVersionForToken(token));
    }

    // helpers
    private Map<String, Object> coerceToMap(Object input) {
        if (input instanceof Map<?, ?> m) {
            Map<String, Object> copy = new LinkedHashMap<>();
            m.forEach((k, v) -> copy.put(String.valueOf(k), v));
            return copy;
        }
        throw new IllegalArgumentException("Input must be a map");
    }
    private String reqStr(Map<String, Object> m, String k) {
        Object v = m.get(k);
        if (v instanceof String s && !s.isBlank()) return s.trim();
        throw new IllegalArgumentException("Missing/invalid string: " + k);
    }
    private String optStr(Map<String, Object> m, String k) {
        Object v = m.get(k);
        return (v instanceof String s && !s.isBlank()) ? s.trim() : null;
    }

    private Map<String, Object> ok(Object payload) {
        Map<String, Object> res = new LinkedHashMap<>();
        res.put("ok", true);
        res.put("data", payload);
        return res;
    }
    private Map<String, Object> error(String code, String msg) {
        Map<String, Object> res = new LinkedHashMap<>();
        res.put("ok", false);
        res.put("error", Map.of("code", code, "message", msg));
        return res;
    }
}
