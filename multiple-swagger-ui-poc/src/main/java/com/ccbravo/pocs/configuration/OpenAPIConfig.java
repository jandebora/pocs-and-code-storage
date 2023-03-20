package com.ccbravo.pocs.configuration;

import java.util.Arrays;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenAPIConfig {
    
    private static final String OAUTH = "OAUTH2";
    private static final String BEARER = "TOKEN";
    
    @Value("${server.servlet.context-path}")
    private String contextPath;
    @Value("${springdoc.swagger-ui.oauth.tokenURL}")
    private String tokenUrl;

    @Autowired
    private BuildProperties buildProperties;
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes(OAUTH, flow())
                        .addSecuritySchemes(BEARER,
                        apiKeySecuritySchema())
                )
                .info(new Info()
                        .version(buildProperties.getVersion())
                        .title("Multiple Swagger UI PoC")
                        .description("RESTful services documentation with OpenAPI 3.")
                )
                .security(Arrays.asList(new SecurityRequirement().addList(OAUTH), new SecurityRequirement().addList(BEARER)))
                .addServersItem(new Server().url(contextPath));
    }

	@Bean
	public GroupedOpenApi detailOpenApi() {
	   String[] paths = {"/v1/entities/{id}"};
	   return GroupedOpenApi.builder().group("detail").pathsToMatch(paths)
	         .build();
	}

	@Bean
    public GroupedOpenApi genericOpenApi() {
       String[] paths = {"/v1/entities"};
       return GroupedOpenApi.builder().group("generic").pathsToMatch(paths)
             .build();
    }
	
	public SecurityScheme apiKeySecuritySchema(String name) {
        return new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER)
                .name(name);
    }
    
    public SecurityScheme apiKeySecuritySchema() {
        return new SecurityScheme()
                .name("Authorization")
                .description("Token from Identity Manager")
                .type(Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }
    
    private SecurityScheme flow() {
        return new SecurityScheme()
                .type(Type.OAUTH2)
                .flows(new OAuthFlows().password(new OAuthFlow().tokenUrl(tokenUrl)));
    }

}
