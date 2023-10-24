package com.bevis.social.core;

import com.bevis.social.core.dto.SocialUserProfile;

import java.util.Map;

public interface SocialConnectionService {
    SocialUserProfile fetchUserProfile(String providerId, String accessToken, Map<String, String> metadata);
}
