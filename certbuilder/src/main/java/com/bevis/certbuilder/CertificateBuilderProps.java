package com.bevis.certbuilder;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "certificate-builder")
class CertificateBuilderProps {
    private String templateName;
}
