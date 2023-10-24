package com.bevis.appbe.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties
public class BevisProperties {

    private Async async;

    @Data
    public static class Async{
        private int corePoolSize;
        private int maxPoolSize;
        private int queueCapacity;
    }
}
