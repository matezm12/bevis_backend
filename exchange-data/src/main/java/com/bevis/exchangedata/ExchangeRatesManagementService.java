package com.bevis.exchangedata;

import com.bevis.exchangedata.domain.ExchangeRate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ExchangeRatesManagementService {
    Page<ExchangeRate> findAll(Pageable pageable);

    Page<ExchangeRate> findAllByCurrencyCode(String search, Pageable pageable);

    void updateCryptoExchangeRatesManually();

    void updateFiatExchangeRatesManually();
}
