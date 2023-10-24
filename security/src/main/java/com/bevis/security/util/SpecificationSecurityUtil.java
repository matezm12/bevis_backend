package com.bevis.security.util;

import com.bevis.security.BevisUser;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static com.bevis.security.AuthoritiesConstants.ADMIN;
import static com.bevis.security.AuthoritiesConstants.VENDOR;
import static com.bevis.security.util.SecurityUtils.getCurrentUser;
import static com.bevis.security.util.SecurityUtils.hasAuthority;

public class SpecificationSecurityUtil {

    public static <T> Predicate getOwnerUserIdPredicate(Root<T> root, CriteriaBuilder criteriaBuilder, String fieldName) {
        BevisUser user = getCurrentUser();
        if (hasAuthority(user, ADMIN)) {
            return criteriaBuilder.conjunction();
        } else if (hasAuthority(user, VENDOR) && Objects.nonNull(user.getGroupAssetId())) {
            Set<String> ownerAssetIdSet = new HashSet<>();
            ownerAssetIdSet.add(user.getGroupAssetId());
            Path<String> ownerIdPath = root.get(fieldName);
            return ownerIdPath.in(ownerAssetIdSet);
        } else {
            return criteriaBuilder.disjunction();
        }
    }
}
