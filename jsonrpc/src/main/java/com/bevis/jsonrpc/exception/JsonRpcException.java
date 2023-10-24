package com.bevis.jsonrpc.exception;

import com.bevis.common.exception.BaseException;

public class JsonRpcException extends BaseException {
    public JsonRpcException(String message) {
        super(message);
    }

    public JsonRpcException(String message, Throwable cause) {
        super(message, cause);
    }
}
