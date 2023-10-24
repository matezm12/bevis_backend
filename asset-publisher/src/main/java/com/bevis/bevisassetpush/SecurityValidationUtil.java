package com.bevis.bevisassetpush;

import com.bevis.bevisassetpush.exception.BevisAssetPushException;
import com.bevis.master.domain.Master;
import com.bevis.user.domain.User;

import java.util.Objects;

import static com.bevis.security.AuthoritiesConstants.ADMIN;

public final class SecurityValidationUtil {

    public static void validateMasterOwnership(Master master, User user) {
        if (isUserAdmin(user)){
            return;
        }
        if (Objects.nonNull(master.getOwnerAssetId()) && !Objects.equals(user.getUserAssetId(), master.getOwnerAssetId())) {
            throw new BevisAssetPushException("Can not access to asset of another owner");
        }
    }

    public static boolean isUserAdmin(User user) {
        return user.getAuthorities()
                .stream()
                .anyMatch(authority -> Objects.equals(authority, ADMIN));
    }
}
