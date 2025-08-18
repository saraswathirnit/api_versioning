package com.carautorox.demo.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    private OpenApiCustomizer v1Customizer() {
        return openApi -> {
            openApi.info(new Info()
                    .title("Car AutoRox Management API - Version 1")
                    .version("v1")
                    .description("View car data and support info using AutoRox API Version 1"));
            openApi.setServers(List.of(new Server()
                    .url("http://localhost:8080")
                    .description("Version 1 Server")));
        };
    }

    private OpenApiCustomizer v2Customizer() {
        return openApi -> {
            openApi.info(new Info()
                    .title("Car AutoRox Management API - Version 2")
                    .version("v2")
                    .description("View car data and support info using AutoRox API Version 2"));
            openApi.setServers(List.of(new Server()
                    .url("http://localhost:8080")
                    .description("Version 2 Server")));
        };
    }

    // ✅ Only register v1 if api.v1.enabled=true
    @Bean
    @ConditionalOnProperty(name = "api.v1.enabled", havingValue = "true", matchIfMissing = true)
    public GroupedOpenApi v1Api() {
        return GroupedOpenApi.builder()
                .group("v1")
                .packagesToScan(
                        "com.carautorox.demo.Version1",
                        "com.carautorox.demo.Authentication.jwt"
                )
                .pathsToMatch("/v1/**", "/support/**")
                .addOpenApiCustomizer(v1Customizer())
                .build();
    }

    // ✅ Only register v2 if api.v2.enabled=true
    @Bean
    @ConditionalOnProperty(name = "api.v2.enabled", havingValue = "true")
    public GroupedOpenApi v2Api() {
        return GroupedOpenApi.builder()
                .group("v2")
                .packagesToScan(
                        "com.carautorox.demo.version2",
                        "com.carautorox.demo.Authentication.jwt"
                )
                .pathsToMatch("/v2/**", "/support/**")
                .addOpenApiCustomizer(v2Customizer())
                .build();
    }

    @Bean
    public OpenAPI customOpenAPIWithSecurity() {
        final String securitySchemeName = "BearerAuth";
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        )
                )
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName));
    }
}

