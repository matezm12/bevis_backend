package com.bevis.resources;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "common.resources")
class ResProps {
    private String baseFolder;
    private String fontsFolder;
}
