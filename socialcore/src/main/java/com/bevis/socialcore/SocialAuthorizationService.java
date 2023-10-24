package com.bevis.socialcore;

import com.bevis.security.dto.JwtToken;

public interface SocialAuthorizationService {

    JwtToken signUpOrLoginWithProvider(SocialLogin socialLogin);
}

