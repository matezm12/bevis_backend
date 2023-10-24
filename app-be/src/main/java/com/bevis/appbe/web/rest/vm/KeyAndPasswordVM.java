package com.bevis.appbe.web.rest.vm;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class KeyAndPasswordVM {

    @NotNull
    private String key;

    @NotNull
    private String newPassword;
}
