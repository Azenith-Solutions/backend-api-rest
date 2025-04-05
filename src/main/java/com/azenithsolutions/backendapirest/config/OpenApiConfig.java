package com.azenithsolutions.backendapirest.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                .openapi("3.0.1")
                .components(new Components()
                        .addSecuritySchemes("basic", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("basic")
                        )
                )
                .info(new Info()
                        .title("Azenith Solutions - HardwareTech | Backend API")
                        .description("Backend Documentation | API RESTful with versioning")
                        .version("1.0")
                )
                .servers(List.of(
                                new Server().url(contextPath).description("API Server")
                        )
                );
    }

    @Bean
    public GroupedOpenApi v1ApiGroup() {
        return GroupedOpenApi.builder()
                .group("v1")
                .pathsToMatch("/v1/**")
                .packagesToScan("com.azenithsolutions.backendapirest.v1")
                .build();
    }
}