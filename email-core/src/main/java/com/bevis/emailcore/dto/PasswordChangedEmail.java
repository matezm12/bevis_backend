package com.bevis.emailcore.dto;

import com.bevis.user.domain.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PasswordChangedEmail {
    private String receiverEmail;
    private User user;
}
