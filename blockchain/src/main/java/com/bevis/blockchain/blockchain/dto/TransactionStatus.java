package com.bevis.blockchain.blockchain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionStatus {
    private String transactionId;
    private Long transactionBlock;
    private Integer confirmationsCount;
}
