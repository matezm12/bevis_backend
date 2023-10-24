package com.bevis.socialcore.impl;

import com.bevis.socialcore.MySocialConnectionService;
import com.bevis.socialcore.SocialAuthorizationService;
import com.bevis.socialcore.SocialLogin;
import com.bevis.security.TokenProvider;
import com.bevis.security.dto.JwtToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
class SocialAuthorizationServiceImpl implements SocialAuthorizationService {

    private final TokenProvider tokenProvider;
    private final MySocialConnectionService mySocialConnectionService;

    @Override
    public JwtToken signUpOrLoginWithProvider(SocialLogin socialLogin) {
        UserDetails userDetails = mySocialConnectionService.signUpOrLoginUserBySocialConnection(socialLogin);
        if (Objects.nonNull(userDetails)) {
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            return tokenProvider.createJwtToken(authentication, false);
        } else {
            log.warn("UserDetails null");
            return null;
        }
    }
}
