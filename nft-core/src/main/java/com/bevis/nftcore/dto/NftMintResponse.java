package com.bevis.nftcore.dto;

public class NftMintResponse {
    private String message;
    private String txHash;
    private String txLink;
    private Object tx;

    public NftMintResponse(String message) {
        this.message = message;
    }

    public NftMintResponse(String message, String txHash, Object tx) {
        this.message = message;
        this.txHash = txHash;
        this.tx = tx;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }

    public String getTxLink() {
        return txLink;
    }

    public void setTxLink(String txLink) {
        this.txLink = txLink;
    }

    public Object getTx() {
        return tx;
    }

    public void setTx(Object tx) {
        this.tx = tx;
    }
}
