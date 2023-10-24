package com.bevis.exchangedata.impl;

import com.bevis.exchangedata.domain.ExchangeRate;
import com.bevis.exchangedata.repository.ExchangeRateRepository;
import com.bevis.exchangedata.CurrencyExchangeUpdater;
import com.bevis.exchangedata.ExchangeRatesManagementService;
import com.bevis.exchangedata.specification.ExchangeRateSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
class ExchangeRatesManagementServiceImpl implements ExchangeRatesManagementService {

    private final ExchangeRateRepository exchangeRateRepository;
    private final CurrencyExchangeUpdater currencyExchangeUpdater;

    @Transactional(readOnly = true)
    @Override
    public Page<ExchangeRate> findAll(Pageable pageable) {
        return exchangeRateRepository.findAll(pageable);
    }

    @Override
    public Page<ExchangeRate> findAllByCurrencyCode(String search, Pageable pageable) {
        return exchangeRateRepository.findAll(ExchangeRateSpecification.bySearchQuery(search), pageable);
    }

    @Override
    public void updateCryptoExchangeRatesManually() {
        currencyExchangeUpdater.updateCryptoCurrencyExchangeRates();
    }

    @Override
    public void updateFiatExchangeRatesManually() {
        currencyExchangeUpdater.updateFiatCurrencyExchangeRates();
    }
}
