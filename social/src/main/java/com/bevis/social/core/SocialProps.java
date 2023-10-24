package com.bevis.social.core;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "social")
class SocialProps {

    private ConnectionProps google;
    private ConnectionProps facebook;
    private AppleConnectionProps apple;

    @Data
    static class ConnectionProps{
        private String clientId;
        private String clientSecret;
    }

    @Data
    static class AppleConnectionProps {
        private String keyId;
        private String teamId;
        private String clientId;
        private String bundleId;
        //Base64 encoded privateKey
        private String privateKey;
    }
}


