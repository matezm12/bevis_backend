package com.bevis.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class BevisUser extends User {

    private String userAssetId;
    private String groupAssetId;

    public BevisUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public BevisUser(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

    public String getUserAssetId() {
        return userAssetId;
    }

    public void setUserAssetId(String userAssetId) {
        this.userAssetId = userAssetId;
    }

    public BevisUser userAssetId(String userAssetId) {
        this.userAssetId = userAssetId;
        return this;
    }

    public String getGroupAssetId() {
        return groupAssetId;
    }

    public void setGroupAssetId(String groupAssetId) {
        this.groupAssetId = groupAssetId;
    }

    public BevisUser groupAssetId(String groupAssetId) {
        this.groupAssetId = groupAssetId;
        return this;
    }
}
