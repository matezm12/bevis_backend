package com.bevis.exchangedata;

import com.bevis.exchange.dto.CurrencyExchangeRate;

import java.util.List;
import java.util.Set;

public interface CurrencyExchangeLoader {
    List<CurrencyExchangeRate> getExchangeRateForCurrencies(Set<String> sourceCryptoCurrencies, String destinationFxCurrency);
    CurrencyExchangeRate getExchangeRateForCurrencies(String sourceCryptoCurrency, String destinationFxCurrency);
}
