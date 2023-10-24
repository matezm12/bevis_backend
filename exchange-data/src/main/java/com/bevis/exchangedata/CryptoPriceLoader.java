package com.bevis.exchangedata;

import com.bevis.exchangedata.dto.CoinPriceDTO;

import java.util.List;

public interface CryptoPriceLoader {
    List<CoinPriceDTO> findAll(String fiatCurrency);
}
