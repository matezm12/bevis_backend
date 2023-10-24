package com.bevis.aws;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "aws.credentials")
class AwsProps {
    private String accessKey;
    private String secretKey;
}
