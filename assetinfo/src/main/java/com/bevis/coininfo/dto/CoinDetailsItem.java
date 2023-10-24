package com.bevis.coininfo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CoinDetailsItem {
    private String fieldTitle;
    private String fieldValue;
    private String link;
    private boolean isAsset;

    public static CoinDetailsItem of(String fieldTitle, String fieldValue) {
        return new CoinDetailsItem(fieldTitle, fieldValue, null, false);
    }
}
