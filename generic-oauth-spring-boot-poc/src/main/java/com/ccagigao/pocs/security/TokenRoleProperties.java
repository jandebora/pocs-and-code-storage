package com.ccagigao.pocs.security;


import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "app.security.token.role")
@Data
public class TokenRoleProperties {
    
    private Map<String, Object> structure;
}
