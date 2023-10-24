package com.bevis.pdf;

import com.bevis.common.exception.BaseException;

public class PdfException extends BaseException {
    public PdfException(String message) {
        super(message);
    }

    public PdfException(String message, Throwable cause) {
        super(message, cause);
    }
}
