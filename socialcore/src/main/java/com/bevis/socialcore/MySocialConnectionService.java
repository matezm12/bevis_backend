package com.bevis.socialcore;

import com.bevis.events.dto.user.UserDataDeleteEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.userdetails.UserDetails;

public interface MySocialConnectionService {
    UserDetails signUpOrLoginUserBySocialConnection(SocialLogin socialLogin);

    @EventListener
    @Order(2)
    void handleUserDataDeleteEvent(UserDataDeleteEvent e);
}
