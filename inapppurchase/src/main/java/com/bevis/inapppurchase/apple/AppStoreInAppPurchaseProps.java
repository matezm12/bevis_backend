package com.bevis.inapppurchase.apple;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "in-app-purchase.app-store")
class AppStoreInAppPurchaseProps {
    private String purchaseMode;
    private String bundleId;
}
