package com.bevis.nft.slp

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class ElectronCashSlpConfig(private val props: ElectronCashSlpProps) {

    @Bean
    open fun getElectronCashSlpApiWebClient(): ElectronCashSlpApiWebClient {
        return if (props.testMode) {
            ElectronCashSlpApiWebClientMock()
        } else {
            ElectronCashSlpApiWebClientImpl(props)
        }
    }
}
