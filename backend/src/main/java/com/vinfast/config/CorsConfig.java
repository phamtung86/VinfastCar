package com.vinfast.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true); // Bắt buộc để gửi Cookie
        config.setAllowedOrigins(List.of("http://localhost:3000")); // Cho phép frontend
        config.setAllowedHeaders(List.of("*")); // Cho phép tất cả headers
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Các method cho phép
        config.addExposedHeader("Authorization"); // Expose Authorization header
        config.addExposedHeader("Set-Cookie");

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
