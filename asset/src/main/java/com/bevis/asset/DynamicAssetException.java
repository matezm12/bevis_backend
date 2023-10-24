package com.bevis.asset;

public class DynamicAssetException extends Exception {
    public DynamicAssetException() {
    }

    public DynamicAssetException(String message) {
        super(message);
    }

    public DynamicAssetException(String message, Throwable cause) {
        super(message, cause);
    }
}
