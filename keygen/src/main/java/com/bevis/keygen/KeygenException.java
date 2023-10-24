package com.bevis.keygen;

import com.bevis.common.exception.BaseException;

public class KeygenException extends BaseException {
    public KeygenException(String message, Exception e) {
        super(message, e);
    }

    public KeygenException(String message) {
        super(message);
    }
}
