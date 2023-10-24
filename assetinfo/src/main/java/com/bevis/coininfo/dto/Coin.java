package com.bevis.coininfo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class Coin {
    private String assetId;
    private String publicKey;
    private String blockchainCode;
    private String blockchainName;
    private String logo;
    private String publicKeyUrl;
    private String zone1; // AssetId
    private String zone2; // Crypto balance
    private String zone3; // Fiat balance
    private String zone4; // For Future
    private String zone5; // For Future
    private String name;  //Need login
    private boolean privateBalance; //Right now only XMR
    private boolean balanceLoaded;
    private boolean hasTokens;
    private Boolean activationStatus;

    private Double cryptoBalance;

    @JsonIgnore
    private Double fiatBalance;
}
