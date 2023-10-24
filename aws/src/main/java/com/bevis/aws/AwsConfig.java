package com.bevis.aws;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
class AwsConfig {

    private final AwsProps awsProps;

    @Bean
    AWSCredentials awsCredentials(){
        log.debug("Configuration AWSCredentials");
        return new BasicAWSCredentials(awsProps.getAccessKey(), awsProps.getSecretKey());
    }
}
