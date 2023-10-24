package com.bevis.webapp.masterdetails.dto;

import lombok.Data;

@Data
public class BlockchainDTO {
    private Long id;
    private String name;
    private String currencyCode;
    private String fullName;
    private boolean privateBalance;
    private Boolean hasTokens;
    private boolean balanceEnable;
    private String ipfsIcon;
}
