package com.bevis.balance.token

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration


@Configuration
@ConfigurationProperties(prefix = "erc20")
internal open class Erc20TokenProps {
    var baseUrl: String? = null
    var apiKey: String? = null
}
