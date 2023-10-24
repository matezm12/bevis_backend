package com.bevis.coininfo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CryptoCoinsInfo {
    private List<Coin> coins;
    private CryptoCoinsMetadata metadata;

    public static CryptoCoinsInfo of(List<Coin> coins, CryptoCoinsMetadata metadata) {
        return new CryptoCoinsInfo(coins, metadata);
    }
}
