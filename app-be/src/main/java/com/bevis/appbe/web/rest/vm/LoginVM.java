package com.bevis.appbe.web.rest.vm;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class LoginVM {

    @NotNull
    private String email;

    @NotNull
    private String password;

    private Boolean rememberMe;
}
