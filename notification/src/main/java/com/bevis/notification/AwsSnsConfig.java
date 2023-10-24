package com.bevis.notification;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@RequiredArgsConstructor
class AwsSnsConfig {
    private final AWSCredentials awsCredentials;
    private final SnsProps snsProps;

    @Bean
    @Profile("dev")
    AmazonSNS amazonSNSClientDev() {
        return AmazonSNSClient.builder()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion(snsProps.getRegion())
                .build();
    }

    @Bean
    @Profile("prod")
    AmazonSNS amazonSNSClientProd() {
        return AmazonSNSClient.builder()
                .withRegion(snsProps.getRegion())
                .build();
    }
}
