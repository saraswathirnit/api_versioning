package com.carautorox.demo.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())

            .authorizeHttpRequests(auth -> auth
                // Allow JWT token generation endpoints
                .requestMatchers("/v1/auth/token", "/v2/auth/token").permitAll()

                // Allow Swagger/OpenAPI endpoints
                .requestMatchers(
                    "/swagger-ui.html",
                    "/swagger-ui/**",
                    "/v3/api-docs/**"
                ).permitAll()

                // ✅ Let interceptor handle all other /v1/** and /v2/** requests
                .requestMatchers("/v1/**", "/v2/**").permitAll()

                // Everything else is blocked
                .anyRequest().authenticated()
            )

            // Disable login forms and HTTP Basic
            .httpBasic(basic -> basic.disable())
            .formLogin(form -> form.disable());

        return http.build();
    }
}
