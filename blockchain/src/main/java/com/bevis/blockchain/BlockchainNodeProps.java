package com.bevis.blockchain;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "blockchain-node")
public class BlockchainNodeProps {
    private String gatewayBaseUrl;
    private String gatewayApiKey;
}
