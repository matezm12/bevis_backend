package com.bevis.security;

import com.bevis.security.dto.JwtToken;

public interface UserJwtService {
    JwtToken authenticate(String email, String password, boolean rememberMe);

    JwtToken refreshToken(String refreshToken, boolean rememberMe);
}
