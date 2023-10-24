package com.bevis.email;

import com.bevis.common.exception.BaseException;

public class EmailException extends BaseException {
    public EmailException(String message, Throwable cause) {
        super(message, cause);
    }
}
