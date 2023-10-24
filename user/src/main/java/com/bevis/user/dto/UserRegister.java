package com.bevis.user.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserRegister {
    @NotNull
    private String email;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
}
