package com.bevis.events.dto.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDataDeleteEvent {
    private Long userId;
}
