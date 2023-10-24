package com.bevis.appbe.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.thymeleaf.templateresolver.UrlTemplateResolver;

@Slf4j
@Configuration
public class ThymeleafConfig {

    @Value("${aws.s3.baseHtmlTemplatesUrl}")
    private String baseHtmlTemplatesUrl;

    @Bean
    @Description("Thymeleaf template resolver serving HTML 5 emails. Loading templates from AWS S3")
    public UrlTemplateResolver awsEmailTemplateResolver() {
        UrlTemplateResolver urlTemplateResolver = new UrlTemplateResolver();
        urlTemplateResolver.setPrefix(baseHtmlTemplatesUrl);
        urlTemplateResolver.setSuffix(".html");
        urlTemplateResolver.setTemplateMode("HTML5");
        urlTemplateResolver.setCharacterEncoding("UTF-8");
        urlTemplateResolver.setOrder(1);
        urlTemplateResolver.setCacheable(false);
        log.debug("ThymeleafConfiguration: Configuring UrlTemplateResolver");
        return urlTemplateResolver;
    }

}
