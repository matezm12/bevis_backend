package com.bevis.security;

import com.bevis.security.dto.JwtToken;
import org.springframework.security.core.Authentication;

public interface TokenProvider {
    JwtToken createJwtToken(Authentication authentication, boolean rememberMe);

    String createToken(Authentication authentication, boolean rememberMe);

    Authentication getAuthentication(String token);

    Authentication getAuthenticationFromRefreshToken(String refreshToken);

    boolean validateToken(String authToken);

}
