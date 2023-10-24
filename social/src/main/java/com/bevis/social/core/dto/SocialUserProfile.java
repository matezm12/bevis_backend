package com.bevis.social.core.dto;

import com.bevis.social.core.Connection;
import lombok.Data;

@Data
public class SocialUserProfile {
    private String id;
    private String name;
    private String firstName;
    private String lastName;
    private String email;
    private String altEmail;
    private String username;
    private String providerId;
    private String providerUserId;
    private String imageUrl;

    private UserProfile userProfile;
    private Connection connection;
}
