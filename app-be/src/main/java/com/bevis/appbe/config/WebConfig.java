package com.bevis.appbe.config;

import com.bevis.versioning.VersionHandlerInterceptor;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@AllArgsConstructor
class WebConfig implements WebMvcConfigurer {

    private final VersionHandlerInterceptor versionHandlerInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(versionHandlerInterceptor);

    }
}
