package com.bevis.social.core;

import com.bevis.social.core.Connection;
import com.bevis.social.core.dto.ConnectionKey;
import com.bevis.social.core.dto.SocialUserProfile;
import com.bevis.social.core.dto.UserProfile;
import org.springframework.stereotype.Component;

@Component
class SocialUserProfileMapper {
    SocialUserProfile mapSocialUserProfile(UserProfile userProfile, Connection connection) {
        ConnectionKey connectionKey = connection.getKey();
        String providerId = connectionKey.getProviderId();
        String providerUserId = connectionKey.getProviderUserId();
        String imageUrl = connection.getImageUrl();

        SocialUserProfile socialUserProfile = new SocialUserProfile();
        socialUserProfile.setId(userProfile.getId());
        socialUserProfile.setName(userProfile.getName());
        socialUserProfile.setFirstName(userProfile.getFirstName());
        socialUserProfile.setLastName(userProfile.getLastName());
        socialUserProfile.setEmail(userProfile.getEmail());
        socialUserProfile.setUsername(userProfile.getUsername());
        socialUserProfile.setImageUrl(imageUrl);
        socialUserProfile.setProviderId(providerId);
        socialUserProfile.setProviderUserId(providerUserId);

        socialUserProfile.setUserProfile(userProfile);
        socialUserProfile.setConnection(connection);
        return socialUserProfile;
    }
}
