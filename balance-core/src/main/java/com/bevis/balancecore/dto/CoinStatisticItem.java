package com.bevis.balancecore.dto;

import com.bevis.common.util.PriceSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoinStatisticItem {
    private String currency;
    private Long coinsCount;
    private Double sumBalance;
    @JsonSerialize(using = PriceSerializer.class)
    private Double sumUsd;

    public CoinStatisticItem(Long coinsCount, Double sumUsd) {
        this.currency = currency;
        this.coinsCount = coinsCount;
        this.sumUsd = sumUsd;
    }
}
