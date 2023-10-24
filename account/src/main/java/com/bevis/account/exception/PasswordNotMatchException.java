package com.bevis.account.exception;

import com.bevis.common.exception.BaseException;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PasswordNotMatchException extends BaseException {
    public PasswordNotMatchException(String message) {
        super(message);
    }
}
