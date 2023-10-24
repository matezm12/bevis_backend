package com.bevis.ipfs.exception;

import com.bevis.common.exception.BaseException;

public class IpfsException extends BaseException {
    public IpfsException(String message) {
        super(message);
    }

    public IpfsException(String message, Throwable cause) {
        super(message, cause);
    }
}
