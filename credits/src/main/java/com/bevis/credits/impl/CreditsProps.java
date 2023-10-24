package com.bevis.credits.impl;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "credits")
class CreditsProps {
    private Boolean paymentsEnabled;
}
