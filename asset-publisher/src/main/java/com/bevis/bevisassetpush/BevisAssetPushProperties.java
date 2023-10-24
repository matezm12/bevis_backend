package com.bevis.bevisassetpush;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "bevis-asset-push")
public class BevisAssetPushProperties {
    private Long maxFileSizeInBytes;
}
