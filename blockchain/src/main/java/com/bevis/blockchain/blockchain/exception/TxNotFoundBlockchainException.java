package com.bevis.blockchain.blockchain.exception;

public class TxNotFoundBlockchainException extends BlockchainException {

    public TxNotFoundBlockchainException(String message) {
        super(message);
    }

    public TxNotFoundBlockchainException(String message, Throwable cause) {
        super(message, cause);
    }
}
