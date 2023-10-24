package com.bevis.notification;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "notification")
class NotificationProps {

    private Sns sns;

    @Data
    static class Sns {
        public String generalTopic;
        private String userActionTopic;
        private String serviceErrorTopic;
    }
}
