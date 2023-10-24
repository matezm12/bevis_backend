package com.bevis.exchangedata.impl;

import com.bevis.blockchaincore.domain.Blockchain;
import com.bevis.exchangedata.domain.ExchangeRate;
import com.bevis.blockchaincore.BlockchainRepository;
import com.bevis.exchangedata.repository.ExchangeRateRepository;
import com.bevis.exchange.ExchangeConstants;
import com.bevis.exchange.crypto.CryptoCurrencyExchangeService;
import com.bevis.exchange.dto.CurrencyExchangeRate;
import com.bevis.exchange.fiat.FiatCurrencyExchangeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
class CurrencyExchangeUpdaterImpl implements com.bevis.exchangedata.CurrencyExchangeUpdater {

    private final ExchangeRateRepository exchangeRateRepository;
    private final FiatCurrencyExchangeService fiatCurrencyExchangeService;
    private final CryptoCurrencyExchangeService cryptoCurrencyExchangeService;
    private final BlockchainRepository blockchainRepository;

    @Override
    public void updateCryptoCurrencyExchangeRates() {
        log.debug("Prepared loading new crypto currency exchanges");
        List<String> supportedCryptoCurrencies = getSupportedCryptoCurrencies();
        log.debug("Loaded crypto-currencies: {}", supportedCryptoCurrencies);
        List<CurrencyExchangeRate> cryptoExchanges = supportedCryptoCurrencies.stream()
                .map(cryptoCurrencyExchangeService::getUsdExchangeRateForCurrency)
                .filter(x -> Objects.nonNull(x.getSourceCurrency()))
                .collect(Collectors.toList());
        log.debug("New crypto currency exchanges loaded successfully");
        log.debug("Preparing saving currency exchanges in database...");
        List<ExchangeRate> exchangeRates = exchangeRateRepository.findAll();
        List<ExchangeRate> changedExchangeRates = cryptoExchanges.stream().map(exchange -> {
            String currencyCode = exchange.getSourceCurrency().toUpperCase();
            Optional<ExchangeRate> rateOpt = exchangeRates.stream()
                    .filter(rate ->
                            Objects.equals(rate.getCurrencyCode().toUpperCase(), currencyCode))
                    .findFirst();
            ExchangeRate exchangeRate = rateOpt.orElse(new ExchangeRate());
            exchangeRate.setCurrencyCode(currencyCode);
            exchangeRate.setUsdPrice(exchange.getValue());
            exchangeRate.setLastModifiedDate(Instant.now());
            log.trace("course: {}", exchangeRate);
            return exchangeRate;
        }).collect(Collectors.toList());
        log.trace("changedCourses: {}", changedExchangeRates);
        saveExchangeRates(changedExchangeRates);
        log.debug("New currency exchanges saved {}", cryptoExchanges);
    }

    @Override
    public void updateFiatCurrencyExchangeRates() {
        Set<String> cryptoCurrencies = new HashSet<>(getSupportedCryptoCurrencies());
        log.debug("Prepared loading new fiat currency exchanges");
        List<CurrencyExchangeRate> fiatExchanges = fiatCurrencyExchangeService.getExchangeRatesFromCurrency(ExchangeConstants.BASE_CURRENCY);
        log.info("New fiat currency exchanges loaded successfully");
        log.debug("Preparing saving fx_rates exchanges in database...");
        List<ExchangeRate> exchangeRates = exchangeRateRepository.findAll();
        List<ExchangeRate> changedExchangeRates = fiatExchanges.stream()
                .filter(rate -> !cryptoCurrencies.contains(rate.getDestinationCurrency().toUpperCase()))
                .map(exchange -> {
                    String currencyCode = exchange.getDestinationCurrency().toUpperCase();
                    Optional<ExchangeRate> rateOpt = exchangeRates.stream()
                            .filter(rate ->
                                    Objects.equals(rate.getCurrencyCode().toUpperCase(), currencyCode))
                            .findFirst();
                    ExchangeRate exchangeRate = rateOpt.orElse(new ExchangeRate());
                    exchangeRate.setCurrencyCode(currencyCode);
                    exchangeRate.setUsdPrice(1.0 / exchange.getValue());
                    exchangeRate.setLastModifiedDate(Instant.now());
                    log.trace("fxRate: {}", exchangeRate);
                    return exchangeRate;
                }).collect(Collectors.toList());
        log.trace("changedCourses: {}", changedExchangeRates);
        saveExchangeRates(changedExchangeRates);
        log.debug("New fx rate exchanges saved {}", fiatExchanges);
    }

    private void saveExchangeRates(List<ExchangeRate> exchangeRates) {
        log.debug("Prepared currency exchanges for saving in database");
        exchangeRateRepository.saveAll(exchangeRates);
    }

    private List<String> getSupportedCryptoCurrencies() {
        return blockchainRepository.findAllByExchangeRatesTrue()
                .stream()
                .map(Blockchain::getCurrencyCode)
                .distinct()
                .collect(Collectors.toList());
    }

}
