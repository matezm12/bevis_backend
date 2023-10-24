package com.bevis.master.dto;

import com.opencsv.bean.CsvBindByPosition;
import lombok.Data;

@Data
public class AssetKeyCsvDTO {

    public static final String[] FIELDS_ORDER = {"key", "asset_id"};

    @CsvBindByPosition(position = 0)
    private String publicKey;

    @CsvBindByPosition(position = 1)
    private String assetId;

}
