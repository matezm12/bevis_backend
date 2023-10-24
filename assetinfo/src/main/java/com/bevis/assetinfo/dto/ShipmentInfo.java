package com.bevis.assetinfo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShipmentInfo {
    private String tracking;
    private String carrier;
    private String destination;
}
