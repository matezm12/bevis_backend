package com.bevis.exchangedata.impl;

import com.bevis.exchangedata.CurrencyExchangeUpdater;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
class CurrencyExchangeUpdatingScheduler {

    private final CurrencyExchangeUpdater currencyExchangeUpdater;

    @Scheduled(fixedRateString = "${exchange-course.refreshTimeIntervalInMilliseconds.cryptoCurrencies}", initialDelay = 60000)
    void updateCryptoCurrencyExchangeTrigger() {
        currencyExchangeUpdater.updateCryptoCurrencyExchangeRates();
        log.info("Scheduled crypto rates updated");
    }

    @Scheduled(fixedRateString = "${exchange-course.refreshTimeIntervalInMilliseconds.fiatCurrencies}", initialDelay = 60000)
    void updateFiatCurrencyExchangeTrigger() {
        currencyExchangeUpdater.updateFiatCurrencyExchangeRates();
        log.info("Scheduled fiat rates updated");
    }
}
