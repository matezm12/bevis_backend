package com.bevis.coininfo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CoinInfo {

    private String sourceCurrency;

    private Double sourceCurrencyBalance;
    
    private String sourceBalance;

    private String destinationCurrency;

    private Double destinationCurrencyBalance;

    private String destinationBalance;

    private Double exchangeRate;

    private boolean hasTokens;
}
