package com.bevis.master.dto;

import lombok.Data;

@Data
public class MasterVendorDTO {
    private Long id;
    private String description;
    private String country;
    private String gps;
    private String filesId;
    private String codereadr;
}
