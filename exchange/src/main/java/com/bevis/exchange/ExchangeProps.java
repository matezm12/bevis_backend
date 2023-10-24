package com.bevis.exchange;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "crypto-exchange")
public class ExchangeProps {
    private String baseUrl;
    private String apiKey;
}
