package com.bevis.webapp.masterdetails.dto;

import lombok.Data;

@Data
public class AssetTypeDTO {
    private Long id;
    private String name;
    private Boolean topupEnable;
    private String description;
}
