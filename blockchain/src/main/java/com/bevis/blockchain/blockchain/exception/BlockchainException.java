package com.bevis.blockchain.blockchain.exception;

import com.bevis.common.exception.BaseException;

public class BlockchainException extends BaseException {
    public BlockchainException(String message) {
        super(message);
    }

    public BlockchainException(String message, Throwable cause) {
        super(message, cause);
    }
}
