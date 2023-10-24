package com.bevis.appbe.web.rest.vm;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RefreshTokenVm {
    @NotNull
    private String refreshToken;
    @NotNull
    private Boolean rememberMe;
}
