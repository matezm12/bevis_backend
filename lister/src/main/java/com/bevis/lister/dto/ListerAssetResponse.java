package com.bevis.lister.dto;

import com.bevis.coininfo.dto.CoinInfo;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ListerAssetResponse {

    private String name;
    private String assetId;
    private LocalDate createdDate;
    private String assetTypeName;
    private String fileUrl;
    private String fileType;
    private Boolean encrypted;

    //Optional Only for Cold Storage Coins
    private CoinInfo coinInfo;

}
