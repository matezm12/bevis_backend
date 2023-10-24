package com.bevis.email;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class AwsSesConfig {
    private final AwsSesProps awsSesProps;

    @Bean("sesAwsCredentials")
    AWSCredentials awsSesCredentials(){
        log.debug("Configuration AWSCredentials");
        return new BasicAWSCredentials(awsSesProps.getCredentials().getAccessKey(), awsSesProps.getCredentials().getSecretKey());
    }

    @Bean
    AmazonSimpleEmailService amazonSimpleEmailServiceClient(){
        log.debug("Configuration AmazonSimpleEmailService");
        return AmazonSimpleEmailServiceClient
                .builder()
                .withCredentials(new AWSStaticCredentialsProvider(awsSesCredentials()))
                .withRegion(awsSesProps.getRegion()).build();
    }
}
