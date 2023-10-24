package com.bevis.assetinfo.dto;

import lombok.Data;

import java.util.Map;

@Data
public class CustomerAssetDTO {

    private String certificateIpfs;
    private Map<String, Object> fields;

    private CustomerAssetDTO(Map<String, Object> record, String certificateIpfs) {
        this.fields = record;
        this.certificateIpfs = certificateIpfs;
    }

    public static CustomerAssetDTO of(Map<String, Object> record, String certificateIpfs) {
        return new CustomerAssetDTO(record, certificateIpfs);
    }
}
