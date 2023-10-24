package com.bevis.certificatecore.exception;

import com.bevis.common.exception.BaseException;

public class CertificateException extends BaseException {

    public CertificateException(String message) {
        super(message);
    }

    public CertificateException(String message, Throwable cause) {
        super(message, cause);
    }
}
