package com.bevis.credits.exception;

import com.bevis.common.exception.BaseException;

public class CreditsChargeException extends BaseException {
    public CreditsChargeException(String message) {
        super(message);
    }

    public CreditsChargeException(String message, Throwable cause) {
        super(message, cause);
    }
}
