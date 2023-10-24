package com.bevis.blockchain.cryptopay.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class TransactionRequest {

    @NotNull
    private String destinationAddress;

    @NotNull
    private BigDecimal amountToPay;

    private String description;

    @NotNull
    private String blockchain;

}
