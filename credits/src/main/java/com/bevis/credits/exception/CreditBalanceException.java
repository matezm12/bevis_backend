package com.bevis.credits.exception;

import com.bevis.common.exception.BaseException;

public class CreditBalanceException extends BaseException {
    public CreditBalanceException() {
    }

    public CreditBalanceException(String message) {
        super(message);
    }

    public CreditBalanceException(String message, Throwable cause) {
        super(message, cause);
    }
}
