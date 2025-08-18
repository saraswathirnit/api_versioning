package com.carautorox.demo.Authentication.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Interceptor to validate JWT tokens and check user → API version access.
 * Custom messages:
 * - Missing token: 401 "Please enter the token"
 * - Invalid/expired token: 401 "Invalid token"
 * - User not allowed for this version: 403 "Access denied: user not allowed for this API version"
 * - Token valid & user allowed: request proceeds normally
 */
public class JwtAuthInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        String authHeader = request.getHeader("Authorization");

        // 1️⃣ Token missing
        if (authHeader == null || authHeader.isBlank() || !authHeader.startsWith("Bearer ")) {
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Please enter the token");
            return false;
        }

        String token = authHeader.substring(7).trim();

        try {
            // 2️⃣ Parse JWT claims
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(JwtTokenController.SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String userId = claims.getSubject();
            String tokenVersion = claims.get("version", String.class);

            // 3️⃣ Determine requested API version from path
            String path = request.getRequestURI();
            String requestedVersion = path.startsWith("/v1/") ? "v1" :
                                      path.startsWith("/v2/") ? "v2" : null;

            if (requestedVersion == null) {
                sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Cannot determine API version from URL");
                return false;
            }

            // 4️⃣ Validate user → version mapping
            Map<String, String> userToVersion = JwtTokenController.userToVersion; // reuse mapping from controller
            String allowedVersion = userToVersion.get(userId);

            if (allowedVersion == null) {
                sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid token: user not recognized");
                return false;
            }

            if (!allowedVersion.equalsIgnoreCase(requestedVersion) && !"both".equalsIgnoreCase(allowedVersion)) {
                sendErrorResponse(response, HttpServletResponse.SC_FORBIDDEN, "Access denied: user not allowed for this API version");
                log.warn("User '{}' with token version '{}' tried to access '{}'", userId, tokenVersion, path);
                return false;
            }

            // ✅ Token valid and user allowed → proceed
            log.info("JWT validated: user={}, tokenVersion={}, requestedPath={}", userId, tokenVersion, path);
            return true;

        } catch (Exception e) {
            // 5️⃣ Token invalid/expired
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
            log.error("JWT validation error: {}", e.getMessage(), e);
            return false;
        }
    }

    private void sendErrorResponse(HttpServletResponse response, int statusCode, String message) {
        try {
            response.setStatus(statusCode);
            response.setContentType("application/json");
            String jsonResponse = String.format("{\"message\": \"%s\"}", message);
            response.getWriter().write(jsonResponse);
        } catch (Exception ex) {
            log.error("Error writing response: {}", ex.getMessage(), ex);
        }
    }
}
