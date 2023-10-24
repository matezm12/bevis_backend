package com.bevis.security;

import com.bevis.security.dto.JwtToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
class UserJwtServiceImpl implements UserJwtService {

    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    @Override
    public JwtToken authenticate(String email, String password, boolean rememberMe) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email, password);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        log.info("Email {} is authenticated: {}", email, authentication.isAuthenticated());
        return tokenProvider.createJwtToken(authentication, rememberMe);
    }

    @Override
    public JwtToken refreshToken(String refreshToken, boolean rememberMe) {
        Authentication authentication = tokenProvider.getAuthenticationFromRefreshToken(refreshToken);
        return tokenProvider.createJwtToken(authentication, rememberMe);
    }

}
