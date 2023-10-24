package com.bevis.appbe.web.rest.vm;

import lombok.Data;

@Data
public class PasswordVM {
    private String currentPassword;
    private String newPassword;
}
