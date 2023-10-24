package com.bevis.blockchain.gateway.dto;

import lombok.Data;

@Data
public class TransactionResponse {
    private String transactionId;
    private Long blockHeight;
    private String blockHash;
    private Integer confirmations;
}
