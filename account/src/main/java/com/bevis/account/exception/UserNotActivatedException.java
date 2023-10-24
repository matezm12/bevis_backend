package com.bevis.account.exception;

import com.bevis.common.exception.BaseException;

public class UserNotActivatedException extends BaseException {
    public UserNotActivatedException(String message) {
        super(message);
    }
}
