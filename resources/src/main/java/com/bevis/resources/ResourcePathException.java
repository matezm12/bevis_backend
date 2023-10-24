package com.bevis.resources;

import com.bevis.common.exception.BaseException;

public class ResourcePathException extends BaseException {
    public ResourcePathException(String message) {
        super(message);
    }

    public ResourcePathException(String message, Throwable cause) {
        super(message, cause);
    }
}
