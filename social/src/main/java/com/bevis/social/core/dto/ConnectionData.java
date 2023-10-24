package com.bevis.social.core.dto;


import java.io.Serializable;

public class ConnectionData implements Serializable {
    private final String providerId;
    private final String providerUserId;
    private final String displayName;
    private final String profileUrl;
    private final String imageUrl;
    private final String accessToken;
    private final String secret;
    private final String refreshToken;
    private final Long expireTime;

    public ConnectionData(String providerId, String providerUserId, String displayName, String profileUrl, String imageUrl, String accessToken, String secret, String refreshToken, Long expireTime) {
        this.providerId = providerId;
        this.providerUserId = providerUserId;
        this.displayName = displayName;
        this.profileUrl = profileUrl;
        this.imageUrl = imageUrl;
        this.accessToken = accessToken;
        this.secret = secret;
        this.refreshToken = refreshToken;
        this.expireTime = expireTime;
    }

    public String getProviderId() {
        return this.providerId;
    }

    public String getProviderUserId() {
        return this.providerUserId;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String getProfileUrl() {
        return this.profileUrl;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public String getSecret() {
        return this.secret;
    }

    public String getRefreshToken() {
        return this.refreshToken;
    }

    public Long getExpireTime() {
        return this.expireTime;
    }

    public int hashCode() {
        int prime = 11;
        int result2 = 1;
        int result = 31 * result2 + (this.accessToken == null ? 0 : this.accessToken.hashCode());
        result = 31 * result + (this.displayName == null ? 0 : this.displayName.hashCode());
        result = 31 * result + (this.expireTime == null ? 0 : this.expireTime.hashCode());
        result = 31 * result + (this.imageUrl == null ? 0 : this.imageUrl.hashCode());
        result = 31 * result + (this.profileUrl == null ? 0 : this.profileUrl.hashCode());
        result = 31 * result + (this.providerId == null ? 0 : this.providerId.hashCode());
        result = 31 * result + (this.providerUserId == null ? 0 : this.providerUserId.hashCode());
        result = 31 * result + (this.refreshToken == null ? 0 : this.refreshToken.hashCode());
        result = 31 * result + (this.secret == null ? 0 : this.secret.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (!(obj instanceof ConnectionData)) {
            return false;
        } else {
            ConnectionData other = (ConnectionData)obj;
            if (this.accessToken == null) {
                if (other.accessToken != null) {
                    return false;
                }
            } else if (!this.accessToken.equals(other.accessToken)) {
                return false;
            }

            if (this.displayName == null) {
                if (other.displayName != null) {
                    return false;
                }
            } else if (!this.displayName.equals(other.displayName)) {
                return false;
            }

            if (this.expireTime == null) {
                if (other.expireTime != null) {
                    return false;
                }
            } else if (!this.expireTime.equals(other.expireTime)) {
                return false;
            }

            if (this.imageUrl == null) {
                if (other.imageUrl != null) {
                    return false;
                }
            } else if (!this.imageUrl.equals(other.imageUrl)) {
                return false;
            }

            if (this.profileUrl == null) {
                if (other.profileUrl != null) {
                    return false;
                }
            } else if (!this.profileUrl.equals(other.profileUrl)) {
                return false;
            }

            if (this.providerId == null) {
                if (other.providerId != null) {
                    return false;
                }
            } else if (!this.providerId.equals(other.providerId)) {
                return false;
            }

            if (this.providerUserId == null) {
                if (other.providerUserId != null) {
                    return false;
                }
            } else if (!this.providerUserId.equals(other.providerUserId)) {
                return false;
            }

            if (this.refreshToken == null) {
                if (other.refreshToken != null) {
                    return false;
                }
            } else if (!this.refreshToken.equals(other.refreshToken)) {
                return false;
            }

            if (this.secret == null) {
                if (other.secret != null) {
                    return false;
                }
            } else if (!this.secret.equals(other.secret)) {
                return false;
            }

            return true;
        }
    }

    public String toString() {
        return "ConnectionData [providerId=" + this.providerId + ", providerUserId=" + this.providerUserId + ", displayName=" + this.displayName + ", profileUrl=" + this.profileUrl + ", imageUrl=" + this.imageUrl + ", accessToken=" + this.accessToken + ", secret=" + this.secret + ", refreshToken=" + this.refreshToken + ", expireTime=" + this.expireTime + "]";
    }
}
