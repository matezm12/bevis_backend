package com.bevis.inapppurchase;

import com.bevis.common.exception.BaseException;

public class InAppPurchaseException extends BaseException {

    public InAppPurchaseException(String message) {
        super(message);
    }

    public InAppPurchaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
