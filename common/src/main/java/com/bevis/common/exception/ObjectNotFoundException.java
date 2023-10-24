package com.bevis.common.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ObjectNotFoundException extends BaseException {
    public ObjectNotFoundException(String message) {
        super(message);
    }
}
