package com.bevis.social.core;

import com.bevis.common.exception.BaseException;

public class SocialConnectionException extends BaseException {

    public SocialConnectionException(String message) {
        super(message);
    }

    public SocialConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
