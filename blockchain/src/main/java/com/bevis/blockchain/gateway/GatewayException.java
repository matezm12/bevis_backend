package com.bevis.blockchain.gateway;

import com.bevis.common.exception.BaseException;

public class GatewayException extends BaseException {
    public GatewayException(String message, Throwable cause) {
        super(message, cause);
    }
}
