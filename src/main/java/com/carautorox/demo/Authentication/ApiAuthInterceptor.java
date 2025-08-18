package com.carautorox.demo.Authentication;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class ApiAuthInterceptor implements HandlerInterceptor, Ordered {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userId = request.getHeader("USER-ID");
        String securityCode = request.getHeader("SECURITY-CODE");

        if (userId == null || securityCode == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"message\": \"API authentication failed. Please enter credentials in USER-ID and SECURITY-CODE.\"}");
            return false;
        }

        if (!UserAuthStore.isValidUser(userId, securityCode)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"message\": \"Invalid USER-ID or SECURITY-CODE.\"}");
            return false;
        }

        return true;
    }

    @Override
    public int getOrder() {
        return 1; // ✅ Priority: first to execute
    }
}
