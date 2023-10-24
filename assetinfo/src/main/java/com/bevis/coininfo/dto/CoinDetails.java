package com.bevis.coininfo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor(staticName = "of")
public class CoinDetails {

    private Coin coin;

    private List<CoinDetailsItem> displayValues;
}
