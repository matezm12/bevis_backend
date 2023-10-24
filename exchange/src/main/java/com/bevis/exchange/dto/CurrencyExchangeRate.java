package com.bevis.exchange.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CurrencyExchangeRate {

    private String sourceCurrency;
    private String destinationCurrency;
    private Double value;
}
