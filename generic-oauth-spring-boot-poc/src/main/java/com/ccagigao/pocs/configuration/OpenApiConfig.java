package com.ccagigao.pocs.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

public class OpenApiConfig {
    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().version("0.0.1-SNAPSHOT").title("Generic OAuth2 Service")
                        .description("RESTful services documentation with OpenAPI 3."))
                .addServersItem(new Server().url(contextPath));
    }

    public SecurityScheme apiKeySecuritySchema() {
        return new SecurityScheme().name("Authorization").description("Description about the TOKEN").type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT").scheme("bearer");
    }
}
