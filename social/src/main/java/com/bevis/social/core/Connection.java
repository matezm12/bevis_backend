package com.bevis.social.core;

import com.bevis.social.core.dto.ConnectionKey;
import com.bevis.social.core.dto.UserProfile;

import java.io.Serializable;

public interface Connection extends Serializable {
    ConnectionKey getKey();

    String getDisplayName();

    String getImageUrl();

    UserProfile fetchUserProfile();

}
