package com.bevis.balance.exception;

public class CryptoBalanceException extends RuntimeException {
    public CryptoBalanceException(String message) {
        super(message);
    }

    public CryptoBalanceException(String message, Throwable cause) {
        super(message, cause);
    }
}
