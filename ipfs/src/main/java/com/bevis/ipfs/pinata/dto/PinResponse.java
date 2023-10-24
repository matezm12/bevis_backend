package com.bevis.ipfs.pinata.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PinResponse {

    @JsonProperty("IpfsHash")
    private String ipfsHash;

    @JsonProperty("PinSize")
    private String pinSize;

    @JsonProperty("Timestamp")
    private String timestamp;
}
