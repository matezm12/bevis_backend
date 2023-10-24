package com.bevis.certificatecore.dto;

import lombok.Data;

@Data
public class CertificateData {
    private String assetId;
    private String dateTimeCreated;
    private String documentFormat;
    private String fileSize;
    private String blockchain;
    private String publicKey;
    private String transactionId;
    private String ipfsCid;
    private String userEmail;
    private String deviceId;
    private String certificateIpfsCid;
    private String ipAddress;
    private String gps;
    private String country;
    private String cityName;
    private String stateName;
    private String phoneBrand;
    private String phoneModel;
    private String operatingSystem;
    private String transactionIdLink;
    private String ipfsCidLink;
    private String certificateIpfsCidLink;
    private String certificateTransactionId;
    private String certificateTransactionIdLink;
    private String sha256Hash;
    private String costInUnits;
    private String block;
}
