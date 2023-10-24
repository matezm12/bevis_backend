package com.bevis.master;

public class PermissionDeniedException extends Exception {
    public PermissionDeniedException(String message) {
        super(message);
    }
}
