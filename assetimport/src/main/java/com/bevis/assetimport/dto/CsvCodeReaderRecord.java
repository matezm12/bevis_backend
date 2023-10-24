package com.bevis.assetimport.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CsvCodeReaderRecord {

    @JsonProperty("Service ID")
    private String serviceId;

    @JsonProperty("Scan ID")
    private String scanId;

    @JsonProperty("Device ID")
    private String deviceId;

    @JsonProperty("Barcode")
    private String barcode;

    @JsonProperty("Timestamp (Scanned)")
    private String timestampScanned;

    @JsonProperty("Timestamp (Received)")
    private String timestampReceived;

    @JsonProperty("Property: gps_location")
    private String propertyName1;

    @JsonProperty("User Name")
    private String userName;

}
