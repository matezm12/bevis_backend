package com.bevis.blockchain.datasign;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "data-sign")
class DataSignProps {
    private String secret;
}
