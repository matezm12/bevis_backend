package com.bevis.coininfo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoinRequest {
    private String publicKey;
    private Double cryptoBalance;
}
