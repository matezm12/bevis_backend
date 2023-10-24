package com.bevis.balance;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "crypto-balance")
public class BalanceProps {
    private String baseUrl;
    private String apiKey;

    public BalanceProps() {
    }

    public String getBaseUrl() {
        return this.baseUrl;
    }

    public String getApiKey() {
        return this.apiKey;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}
