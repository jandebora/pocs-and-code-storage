package com.ccbravo.pocs.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenAPIConfig {

    @Value("${server.servlet.context-path}")
	private String contextPath;
    
    @Bean
	public OpenAPI customOpenAPI() {
		// define the apiKey SecuritySchema
		return new OpenAPI()
				.info(new Info().version("0.0.1-SNAPSHOT").title("Webflux Service 2")
						.description("RESTful services documentation with OpenAPI 3."))
				.addServersItem(new Server().url(contextPath));
	}
	
}
