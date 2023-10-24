package com.bevis.exchangedata.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CoinPriceDTO {
    private String cryptoCurrency;
    private String fiatCurrency;
    private Double cryptoPrice;
}
