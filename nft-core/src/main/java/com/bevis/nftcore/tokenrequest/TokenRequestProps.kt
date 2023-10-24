package com.bevis.nftcore.tokenrequest

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "nft-core.token-request")
open class TokenRequestProps {
    var chunkSize: Int = 18
}
