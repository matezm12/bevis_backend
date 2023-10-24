package com.bevis.security.util;

import com.bevis.common.exception.PermissionDeniedException;
import com.bevis.security.BevisUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Objects;
import java.util.Optional;

import static com.bevis.security.AuthoritiesConstants.ADMIN;
import static com.bevis.security.AuthoritiesConstants.SUPERADMIN;

@Slf4j
public final class SecurityUtils {
    public static Optional<String> getCurrentLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
                .map(authentication -> {
                    if (authentication.getPrincipal() instanceof UserDetails) {
                        UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
                        return springSecurityUser.getUsername();
                    } else if (authentication.getPrincipal() instanceof String) {
                        return (String) authentication.getPrincipal();
                    }
                    return null;
                });
    }

    public static boolean isCurrentUserInRole(String authority) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
                .map(authentication -> authentication.getAuthorities().stream()
                        .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(authority)))
                .orElse(false);
    }

    public static boolean isCurrentUserAdmin() {
        return isCurrentUserInRole(ADMIN) || isCurrentUserInRole(SUPERADMIN);
    }

    public static BevisUser getCurrentUser() {
        return tryToGetCurrentUser().orElse(null);
    }

    public static Optional<BevisUser> tryToGetCurrentUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
                .map(Authentication::getPrincipal)
                .map(principal -> (principal instanceof BevisUser ? (BevisUser) principal : null));
    }

    public static boolean hasAuthority(BevisUser user, String... authorities) {
        if (user != null) {
            SecurityContext securityContext = SecurityContextHolder.getContext();
            for (String authority : authorities) {
                return securityContext.getAuthentication().getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .anyMatch(a -> a.equals(authority));
            }
        }
        return false;
    }

    public static <T> T checkPermissions(T obj, String ownerAssetId) {
        BevisUser currentUser = getCurrentUser();
        if (!hasAuthority(currentUser, ADMIN) && !Objects.equals(ownerAssetId, currentUser.getGroupAssetId())) {
            log.debug("Access denied");
            throw new PermissionDeniedException("Access denied!");
        }
        return obj;
    }
}
