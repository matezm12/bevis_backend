package com.bevis.blockchain.datasign;

public interface DataSignService {
    String signTxData(String unsignedData, String address);
    boolean validateSignedData(String signedData, String address);
}
