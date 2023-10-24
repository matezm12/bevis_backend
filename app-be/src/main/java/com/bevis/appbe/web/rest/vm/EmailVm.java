package com.bevis.appbe.web.rest.vm;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class EmailVm {
    @NotNull
    private String email;
}
