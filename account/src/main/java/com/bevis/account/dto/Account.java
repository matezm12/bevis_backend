package com.bevis.account.dto;

import com.bevis.user.domain.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Account {
    private User user;
}
