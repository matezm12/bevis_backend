package com.bevis.user.impl;

import com.bevis.common.util.RandomUtil;
import com.bevis.events.EventPublishingService;
import com.bevis.security.AuthoritiesConstants;
import com.bevis.user.UserRegisterService;
import com.bevis.user.UserService;
import com.bevis.user.domain.User;
import com.bevis.user.dto.OnUserRegisteredEvent;
import com.bevis.user.dto.UserRegister;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
class UserRegisterServiceImpl implements UserRegisterService {

    private final UserService userService;
    private final EventPublishingService eventPublishingService;

    @Transactional
    @Override
    public User registerUser(UserRegister userRegister, String password, boolean activated) {
        User user = userService.createUser(
                User.builder()
                        .firstName(userRegister.getFirstName())
                        .lastName(userRegister.getLastName())
                        .email(userRegister.getEmail())
                        .authority(AuthoritiesConstants.USER)
                        .activated(activated)
                        .activationKey(!activated ? RandomUtil.generateActivationKey() : null)
                        .build(),
                password
        );
        eventPublishingService.publishEvent(new OnUserRegisteredEvent(user));
        return user;
    }
}
