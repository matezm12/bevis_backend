package com.bevis.appbe.web.rest.common;

import com.bevis.security.UserJwtService;
import com.bevis.security.dto.JwtToken;
import com.bevis.socialcore.SocialAuthorizationService;
import com.bevis.socialcore.SocialLogin;
import com.bevis.appbe.web.rest.vm.LoginVM;
import com.bevis.appbe.web.rest.vm.RefreshTokenVm;
import com.bevis.appbe.web.rest.vm.SocialLoginVM;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(description="Authentication module")
@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserJwtController {

    private final UserJwtService userJwtService;
    private final SocialAuthorizationService socialAuthorizationService;

    @ApiOperation(value = "Sign in with credentials (login,password)")
    @PostMapping("/authenticate")
    JwtToken authenticate(@Valid @RequestBody LoginVM loginVM){
        boolean rememberMe = loginVM.getRememberMe() != null && loginVM.getRememberMe();
        String username = loginVM.getEmail().toLowerCase();
        String password = loginVM.getPassword();
        return userJwtService.authenticate(username, password, rememberMe);
    }

    @ApiOperation(
            value = "Sign up with Google, Facebook, etc. ",
            notes = "Possible three cases:\n" +
                    "1. Sing in if account and connection already exists\n" +
                    "2. Create new social connection if account exists but social connection none.\n" +
                    "3. Create new account if not exists)"
    )
    @PostMapping(value = "/social/{provider}/sign-up")
    JwtToken signUpOrLoginWithSocialProvider(
            @ApiParam(value = "value: \"google\" for Google SignIn; value: \"facebook\" for Facebook SignIn; ")
            @PathVariable String provider,
            @Valid @RequestBody SocialLoginVM loginVM) {
        SocialLogin socialLogin = new SocialLogin();
        socialLogin.setProvider(provider);
        socialLogin.setAccessToken(loginVM.getAccessToken());
        socialLogin.setEmail(loginVM.getEmail());
        socialLogin.setMetadata(loginVM.getMetadata());
        return socialAuthorizationService.signUpOrLoginWithProvider(socialLogin);
    }

    @PostMapping("/authenticate/refresh")
    JwtToken refreshToken(@Valid @RequestBody RefreshTokenVm refreshTokenVm){
        String refreshToken = refreshTokenVm.getRefreshToken();
        Boolean rememberMe = refreshTokenVm.getRememberMe();
        return userJwtService.refreshToken(refreshToken, rememberMe);
    }
}
