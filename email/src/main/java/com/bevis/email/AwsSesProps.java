package com.bevis.email;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "aws.ses")
class AwsSesProps {
    private String region;
    private Credentials credentials;

    @Data
    static class Credentials {
        private String accessKey;
        private String secretKey;
    }
}
