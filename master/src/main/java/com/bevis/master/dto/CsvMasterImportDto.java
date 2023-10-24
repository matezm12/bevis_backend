package com.bevis.master.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CsvMasterImportDto {

    @JsonProperty("WIF")
    private String wif;

    @JsonProperty("Address")
    private String address;
}
