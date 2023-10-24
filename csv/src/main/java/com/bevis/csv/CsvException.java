package com.bevis.csv;

import com.bevis.common.exception.BaseException;

public class CsvException extends BaseException {
    public CsvException(String message) {
        super(message);
    }

    public CsvException(String message, Throwable cause) {
        super(message, cause);
    }
}
