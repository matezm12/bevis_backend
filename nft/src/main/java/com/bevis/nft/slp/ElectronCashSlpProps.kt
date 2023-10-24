package com.bevis.nft.slp

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "nft.slp")
open class ElectronCashSlpProps {
    lateinit var baseUrl: String
    lateinit var apiKey: String
    var testMode: Boolean = true
}
