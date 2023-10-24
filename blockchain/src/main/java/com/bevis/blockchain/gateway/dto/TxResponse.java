package com.bevis.blockchain.gateway.dto;

import lombok.Data;

@Data
public class TxResponse {
    private String tx;
    private Boolean result;
}
