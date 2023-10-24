package com.bevis.exchange.fiat;

import lombok.Data;

@Data
class RateItemDTO {
    private String currency;
    private Double rate;
}
