package com.bevis.asset.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
public class AssetStatisticDTO {
    private String keyName;
    private Object count;
}
