package com.bevis.credits.exception;

public class DuplicateCreditsChargeException extends CreditsChargeException {
    public DuplicateCreditsChargeException(String message) {
        super(message);
    }

    public DuplicateCreditsChargeException(String message, Throwable cause) {
        super(message, cause);
    }
}
