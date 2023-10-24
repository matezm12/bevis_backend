package com.bevis.notification;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("aws.sns")
class SnsProps {
    private String region;
}
