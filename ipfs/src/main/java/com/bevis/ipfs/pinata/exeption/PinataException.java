package com.bevis.ipfs.pinata.exeption;

import com.bevis.common.exception.BaseException;

public class PinataException extends BaseException {
    public PinataException(String message) {
        super(message);
    }

    public PinataException(String message, Throwable cause) {
        super(message, cause);
    }
}
