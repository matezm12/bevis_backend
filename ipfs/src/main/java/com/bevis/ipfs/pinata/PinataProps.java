package com.bevis.ipfs.pinata;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "pinata")
class PinataProps {
    private String apiKey;
    private String secretApiKey;
}
