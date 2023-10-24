package com.bevis.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    static final String AUTHORIZATION_HEADER = "Authorization";

    static final String AUTHORIZATION_TOKEN = "access_token";

    private final JwtFilter jwtFilter;

    @Override
    public void configure(HttpSecurity http) {
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
