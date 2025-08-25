package com.carautorox.demo.mcp;

import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class McpResourceHandler {

    private final McpToolRegistry registry;

    public McpResourceHandler(McpToolRegistry registry) {
        this.registry = registry;
    }

    public Object execute(String toolName, Object args) {
        try {
            return registry.get(toolName)
                    .map(h -> ok(h.execute(args)))
                    .orElseGet(() -> error("UNKNOWN_TOOL", "No such tool: " + toolName));
        } catch (IllegalArgumentException iae) {
            return error("VALIDATION_ERROR", iae.getMessage());
        } catch (Exception ex) {
            return error("SERVER_ERROR", ex.getMessage());
        }
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
