package com.bevis.balancecore.dto;

import com.bevis.common.util.PriceSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoinBalanceItem {
    private String currency;
    private String publicKey;
    private Double cryptoBalance;
    @JsonSerialize(using = PriceSerializer.class)
    private Double usdBalance;
    private Instant lastScanned;
    private Instant createdAt;
    private Instant updatedAt;
}
