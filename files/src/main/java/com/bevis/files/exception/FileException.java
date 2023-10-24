package com.bevis.files.exception;

import com.bevis.common.exception.BaseException;


public class FileException extends BaseException {
    public FileException(String message) {
        super(message);
    }

    public FileException(String message, Throwable cause) {
        super(message, cause);
    }
}
