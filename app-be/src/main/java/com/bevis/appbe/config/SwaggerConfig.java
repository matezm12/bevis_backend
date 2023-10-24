package com.bevis.appbe.config;

import com.google.common.base.Predicates;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
@Profile("dev")
public class SwaggerConfig {

    @Value("${app.name}")
    private String appName;

    @Value("${api.rest.current-version}")
    private String currentVersion;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(Predicates.not(PathSelectors.regex("/error.*")))
                .build()
                .apiInfo(apiInfo())
                .securitySchemes(Collections.singletonList(apiKey()));
    }
    private ApiKey apiKey() {
        return new ApiKey("token", "Authorization", "header");
    }
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title(appName)
                .description("The REST API.").termsOfServiceUrl("")
                .version(currentVersion)
                .build();
    }
}
