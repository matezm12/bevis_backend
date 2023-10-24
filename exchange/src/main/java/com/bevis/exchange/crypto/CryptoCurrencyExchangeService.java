package com.bevis.exchange.crypto;

import com.bevis.exchange.dto.CurrencyExchangeRate;

public interface CryptoCurrencyExchangeService {

    /**
     *
     * @param sourceCurrency BTC, ETH, ... etc
     * @param destinationCurrency USD, EUR, ... etc
     * @return CurrencyExchangeRate {sourceCurrency, destinationCurrency, value}
     * If error, then return CurrencyExchangeRate {null, null, null}
     */
    CurrencyExchangeRate getExchangeRateForCurrencies(String sourceCurrency, String destinationCurrency);

    /**
     *
     * @param sourceCurrency: BTC, ETH, ... etc
     * @return CurrencyExchangeRate {sourceCurrency, destinationCurrency, value}
     * If error, then return CurrencyExchangeRate {null, null, null}
     */
    CurrencyExchangeRate getUsdExchangeRateForCurrency(String sourceCurrency);
}
