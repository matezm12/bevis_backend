package com.bevis.balance.coinbalance

import com.bevis.balance.coinbalance.dto.BalanceSource
import org.springframework.stereotype.Service

@Service
class CryptoBalanceApiConfig {
    private val currencySourceMap: MutableMap<String, String> = HashMap()
    private val allSources: MutableMap<String, MutableMap<String, BalanceSource>> = HashMap()

    fun getDefaultCurrencySource(currency: String): String? = currencySourceMap[currency]

    fun getBalanceSource(currency: String, source: String): BalanceSource? = allSources[currency]?.get(source)

    fun setBalanceSources(currency: String, sources: List<BalanceSource>) {
        allSources[currency] = HashMap(sources.associateBy { it.source })
    }

    fun setBalanceSource(currency: String, source: BalanceSource) {
        if (!allSources.containsKey(currency)) {
            allSources[currency] = HashMap()
        }
        allSources[currency]?.set(source.source, source)
    }

    fun removeBalanceSource(currency: String, source: String) {
        allSources[currency]?.remove(source)
    }

    fun setDefaultCurrencySource(currency: String, source: String) {
        currencySourceMap[currency] = source
    }
}
