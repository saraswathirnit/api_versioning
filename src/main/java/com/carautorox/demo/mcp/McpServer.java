package com.carautorox.demo.mcp;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/mcp")
public class McpServer {

    private final McpResourceHandler handler;
    private final McpToolRegistry registry;

    public McpServer(McpResourceHandler handler, McpToolRegistry registry) {
        this.handler = handler;
        this.registry = registry;
    }

    @GetMapping("/tools")
    public ResponseEntity<Object> listTools() {
        return ResponseEntity.ok(Map.of("ok", true, "tools", registry.listNames()));
    }

    @PostMapping("/execute")
    public ResponseEntity<Object> execute(@RequestBody Map<String, Object> body,
                                          @RequestHeader(value = "Authorization", required = false) String auth) {
        // Pass-through auth is not used by tools here (your tools read auth from args if needed).
        String tool = (String) body.get("tool");
        Object input = body.get("input");
        Object result = handler.execute(tool, input);
        return ResponseEntity.ok(result);
    }
}
