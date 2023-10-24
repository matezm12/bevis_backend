package com.bevis.email.sendgrid;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "email.sendgrid")
public class SendGridProps {
    private String apiKey;
}
