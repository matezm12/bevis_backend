package com.bevis.keygen;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "crypto-wallet-generator")
class CryptoWalletGeneratorProps {
    private String awsLambdaUrl;
    private String awsLambdaApiKey;

    public String getAwsLambdaUrl() {
        return awsLambdaUrl;
    }

    public void setAwsLambdaUrl(String awsLambdaUrl) {
        this.awsLambdaUrl = awsLambdaUrl;
    }

    public String getAwsLambdaApiKey() {
        return awsLambdaApiKey;
    }

    public void setAwsLambdaApiKey(String awsLambdaApiKey) {
        this.awsLambdaApiKey = awsLambdaApiKey;
    }
}
