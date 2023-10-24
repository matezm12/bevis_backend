package com.bevis.common.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BaseException extends RuntimeException {
    public BaseException(String message) {
        super(message);
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
