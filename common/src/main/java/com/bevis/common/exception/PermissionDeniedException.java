package com.bevis.common.exception;

public class PermissionDeniedException extends BaseException {

    public PermissionDeniedException() {
        super("You are not authorized to do this action");
    }

    public PermissionDeniedException(String message) {
        super(message);
    }

    public PermissionDeniedException(String message, Throwable cause) {
        super(message, cause);
    }
}
