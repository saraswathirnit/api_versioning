package com.carautorox.demo.mcp;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class McpToolRegistry {
    private final Map<String, McpToolHandler> tools = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    public McpToolRegistry(ApplicationContext ctx) {
        ctx.getBeansOfType(McpToolHandler.class)
          .values()
          .forEach(h -> tools.put(h.getToolName(), h));
    }

    public Optional<McpToolHandler> get(String name) { return Optional.ofNullable(tools.get(name)); }
    public List<String> listNames() { return List.copyOf(tools.keySet()); }
}
