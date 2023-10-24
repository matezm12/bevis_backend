package com.bevis.appbe.web.rest.vm;

import lombok.Data;

import java.io.Serializable;

@Data
public class FieldErrorVM implements Serializable {

    private final String objectName;

    private final String field;

    private final String message;

}
