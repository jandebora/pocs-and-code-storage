package com.ccbravo.pocs.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "service2.consumers")
@Data
public class Service2ConsumerConfigProperties {
    
    private Service1Config service1;
    
    @Data
    public static class Service1Config {
        private String baseUrl;
        private Paths paths;
    }
    
    @Data
    public static class Paths {
        private String messages;
    }
}
