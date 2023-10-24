package com.bevis.bevisassetpush.exception;

import com.bevis.common.exception.BaseException;

public class BevisAssetPushException extends BaseException {
    public BevisAssetPushException(String message) {
        super(message);
    }

    public BevisAssetPushException(String message, Throwable cause) {
        super(message, cause);
    }
}
