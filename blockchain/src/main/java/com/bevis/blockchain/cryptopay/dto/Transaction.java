package com.bevis.blockchain.cryptopay.dto;

import lombok.Data;

@Data
public class Transaction {
    private Boolean processed;
    private String transactionId;
    private String blockchain;
    private String error;
}
