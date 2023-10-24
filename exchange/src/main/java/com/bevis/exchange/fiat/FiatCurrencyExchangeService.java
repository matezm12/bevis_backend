package com.bevis.exchange.fiat;

import com.bevis.exchange.dto.CurrencyExchangeRate;

import java.util.List;

public interface FiatCurrencyExchangeService {
    List<CurrencyExchangeRate> getExchangeRatesFromCurrency(String currency);

    CurrencyExchangeRate getExchangeRateForCurrencies(String sourceCurrency, String destinationCurrency);
}
