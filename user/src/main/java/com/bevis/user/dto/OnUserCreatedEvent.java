package com.bevis.user.dto;

import com.bevis.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OnUserCreatedEvent {
    private User user;
    private String password;
}
