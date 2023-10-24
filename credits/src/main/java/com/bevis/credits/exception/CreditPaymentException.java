package com.bevis.credits.exception;

import com.bevis.common.exception.BaseException;

public class CreditPaymentException extends BaseException {
    public CreditPaymentException() {
    }

    public CreditPaymentException(String message) {
        super(message);
    }

    public CreditPaymentException(String message, Throwable cause) {
        super(message, cause);
    }
}
