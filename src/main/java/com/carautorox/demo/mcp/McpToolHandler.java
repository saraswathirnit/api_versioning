package com.carautorox.demo.mcp;

/**
 * Base interface for all MCP tools.
 */
public interface McpToolHandler {

    /** Unique, human-readable tool name (used as registry key). */
    String getToolName();

    /**
     * Generic execution entrypoint for the tool.
     * You can pass any input shape (DTO/Map/etc) and return any output.
     */
    Object execute(Object input);
}
