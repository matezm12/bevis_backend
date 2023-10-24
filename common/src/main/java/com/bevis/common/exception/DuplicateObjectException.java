package com.bevis.common.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DuplicateObjectException extends BaseException {
    public DuplicateObjectException(String message) {
        super(message);
    }
}
