package com.bevis.appbe.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration("corsConfiguration")
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "DELETE", "POST", "PUT", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(false).maxAge(3600);
    }

}
