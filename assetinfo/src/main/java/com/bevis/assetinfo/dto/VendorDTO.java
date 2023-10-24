package com.bevis.assetinfo.dto;

import lombok.Data;

import java.util.List;

@Data
public class VendorDTO {

    private Long id;

    private String assetId;

    private String description;

    private String country;

    private String gps;

    private String img;

    private List<String> imgs;
}
