package com.bevis.exchange.fiat;

import lombok.Data;

import java.util.List;

@Data
class FiatExchangesDTO {
    private String base;
    private List<RateItemDTO> rates;
}
