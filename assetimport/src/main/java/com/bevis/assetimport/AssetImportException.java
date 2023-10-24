package com.bevis.assetimport;

public class AssetImportException extends Exception {
    public AssetImportException() {
    }

    public AssetImportException(String message) {
        super(message);
    }

    public AssetImportException(String message, Throwable cause) {
        super(message, cause);
    }
}
