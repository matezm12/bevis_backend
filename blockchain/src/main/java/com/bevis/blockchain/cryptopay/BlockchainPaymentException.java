package com.bevis.blockchain.cryptopay;

import com.bevis.common.exception.BaseException;

public class BlockchainPaymentException extends BaseException {

    public BlockchainPaymentException(String message) {
        super(message);
    }

    public BlockchainPaymentException(String message, Throwable cause) {
        super(message, cause);
    }
}
