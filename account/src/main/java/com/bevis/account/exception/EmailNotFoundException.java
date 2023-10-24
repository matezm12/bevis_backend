package com.bevis.account.exception;

import com.bevis.common.exception.ObjectNotFoundException;

public class EmailNotFoundException extends ObjectNotFoundException {
    public EmailNotFoundException(String message) {
        super(message);
    }
}
