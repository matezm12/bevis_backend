package com.bevis.exchange.crypto;

import lombok.Data;

@Data
class CryptoExchangeDTO {
    private String base;
    private String target;
    private Double rate;
}
