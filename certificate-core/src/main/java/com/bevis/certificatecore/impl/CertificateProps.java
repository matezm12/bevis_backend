package com.bevis.certificatecore.impl;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "certificate")
class CertificateProps {
    private String resourcesFolder;
}
