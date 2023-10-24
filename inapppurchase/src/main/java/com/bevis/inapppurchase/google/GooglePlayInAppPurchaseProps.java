package com.bevis.inapppurchase.google;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "in-app-purchase.google-play")
class GooglePlayInAppPurchaseProps {
    private String appName;
    private String packageName;
    private String jsonLocation;
}
