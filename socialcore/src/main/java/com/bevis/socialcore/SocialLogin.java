package com.bevis.socialcore;

import lombok.Data;

import java.util.Map;

@Data
public class SocialLogin {
    private String provider;
    private String accessToken;
    private String email;
    private Map<String, String> metadata;
}
