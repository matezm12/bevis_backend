package com.bevis.exchangedata.impl;

import com.bevis.common.exception.ObjectNotFoundException;
import com.bevis.exchangedata.domain.ExchangeRate;
import com.bevis.exchangedata.repository.ExchangeRateRepository;
import com.bevis.exchange.ExchangeConstants;
import com.bevis.exchange.dto.CurrencyExchangeRate;
import com.bevis.exchangedata.CurrencyExchangeLoader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.bevis.common.util.StringUtil.toUpperCase;

@Service
@Slf4j
@RequiredArgsConstructor
public class CurrencyExchangeLoaderImpl implements CurrencyExchangeLoader {

    private final ExchangeRateRepository exchangeRateRepository;

    @Override
    public CurrencyExchangeRate getExchangeRateForCurrencies(String sourceCryptoCurrency, String destinationFxCurrency) {
        return getExchangeRateForCurrencies(Collections.singleton(sourceCryptoCurrency), destinationFxCurrency)
                .stream().findFirst()
                .orElseThrow(() -> {
                    log.warn("Currency " + sourceCryptoCurrency + " not found");
                    return new ObjectNotFoundException("Currency " + sourceCryptoCurrency + " not found");
                });
    }

    @Override
    public List<CurrencyExchangeRate> getExchangeRateForCurrencies(Set<String> sourceCryptoCurrencies, String destinationFxCurrency) {
        List<ExchangeRate> allCrypto = exchangeRateRepository.findAllByCurrencyCodeIn(sourceCryptoCurrencies);

        Double destinationFxPriceInUsd = calculateCurrencyPriceInUsd(destinationFxCurrency)
                .orElseThrow(() -> {
                    log.warn("Currency " + destinationFxCurrency + " not found");
                    return new ObjectNotFoundException("Currency " + destinationFxCurrency + " not found");
                });

        return allCrypto.stream()
                .map(x -> CurrencyExchangeRate.builder()
                        .sourceCurrency(x.getCurrencyCode())
                        .destinationCurrency(destinationFxCurrency)
                        .value(x.getUsdPrice() / destinationFxPriceInUsd)
                        .build())
                .collect(Collectors.toList());
    }

    private Optional<Double> calculateCurrencyPriceInUsd(String currencyCode) {
        if (Objects.equals(ExchangeConstants.BASE_CURRENCY, toUpperCase(currencyCode))) {
            return Optional.of(1.0); // Destination currency is USD (BASE_CURRENCY)
        } else {
            return exchangeRateRepository.findByCurrencyCode(currencyCode)
                    .map(ExchangeRate::getUsdPrice);
        }
    }

}
