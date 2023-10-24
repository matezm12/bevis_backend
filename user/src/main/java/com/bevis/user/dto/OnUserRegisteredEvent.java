package com.bevis.user.dto;

import com.bevis.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OnUserRegisteredEvent {
    private User user;
}
