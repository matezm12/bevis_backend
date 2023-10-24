package com.bevis.inapppurchase.google;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.androidpublisher.AndroidPublisher;
import com.google.api.services.androidpublisher.AndroidPublisherScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Objects;

@Configuration
@Slf4j
@RequiredArgsConstructor
class GooglePlayInAppPurchaseConfig {

    private final GooglePlayInAppPurchaseProps  purchaseProps;

    @Bean(name = "financialGoogleCredentials")
    GoogleCredentials googleCredentials() {
        try {
            return GoogleCredentials
                    .fromStream(new FileInputStream(purchaseProps.getJsonLocation()))
                    .createScoped(AndroidPublisherScopes.ANDROIDPUBLISHER);
        } catch (IOException e) {
            log.warn("GoogleCredentials not configured!");
            return null;
        }
    }

    @Bean
    AndroidPublisher androidPublisher() throws GeneralSecurityException, IOException {
        GoogleCredentials credentials = googleCredentials();
        if (Objects.nonNull(credentials)) {
            return new AndroidPublisher.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JacksonFactory.getDefaultInstance(),
                    new HttpCredentialsAdapter(credentials)
            ).setApplicationName(purchaseProps.getAppName()).build();
        } else {
            log.warn("AndroidPublisher not configured!");
            return null;
        }
    }
}
