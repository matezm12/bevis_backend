package com.bevis.appbe.web.rest.vm;

import com.bevis.user.dto.UserRegister;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ManagedUserVM extends UserRegister {

    @NotNull
    private String password;
}
