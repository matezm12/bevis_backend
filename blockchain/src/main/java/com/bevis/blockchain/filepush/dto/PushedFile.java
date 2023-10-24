package com.bevis.blockchain.filepush.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PushedFile {
    private String ipfsHash;
    private String transactionId;
    private String blockchain;
}
