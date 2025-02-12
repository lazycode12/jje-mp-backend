package com.jmp.gestion_notes.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // Allow all endpoints for development
                registry.addMapping("/**")
                    .allowedOrigins("http://localhost:4200") // Angular's URL
                    .allowedMethods("GET", "POST", "PUT", "DELETE");
            }
        };
    }
}
