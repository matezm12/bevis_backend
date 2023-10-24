package com.bevis.user.impl;

import com.bevis.user.domain.User;
import com.bevis.user.dto.UserFilter;
import com.google.common.base.Strings;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Arrays;
import java.util.Objects;

public final class UserSpecification {

    private final static String[] SEARCH_FIELDS = new String[]{ "email", "firstName", "lastName"};

    public static Specification<User> bySearchQuery(UserFilter filter) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.and(
                getSearchPredicate(root, criteriaBuilder, filter.getSearch()),
                getAuthorityFilterPredicate(root, criteriaBuilder, filter.getAuthority()),
                getActivatedOnlyPredicate(root, criteriaBuilder, filter.getActivatedOnly())
        );
    }

    private static Predicate getActivatedOnlyPredicate(Root<User> root, CriteriaBuilder criteriaBuilder, Boolean activatedOnly) {
        if (Objects.isNull(activatedOnly) || !activatedOnly) {
            return criteriaBuilder.conjunction();
        }
        Path<Boolean> activatedPath = root.get("activated");
        return criteriaBuilder.equal(activatedPath, true);
    }

    private static Predicate getAuthorityFilterPredicate(Root<User> root, CriteriaBuilder criteriaBuilder, String authority) {
        if (Strings.isNullOrEmpty(authority)) {
            return criteriaBuilder.conjunction();
        }
        Path<String> authorityPath = root.get("authority");
        return criteriaBuilder.equal(authorityPath, authority);
    }

    private static Predicate getSearchPredicate(Root<User> root, CriteriaBuilder criteriaBuilder, String search) {
        if (Strings.isNullOrEmpty(search)) {
            return criteriaBuilder.conjunction();
        }
        return Arrays.stream(SEARCH_FIELDS)
                .map(fieldName -> getStringPredicate(root, criteriaBuilder, search, fieldName))
                .reduce(criteriaBuilder::or)
                .orElse(criteriaBuilder.conjunction());
    }

    private static Predicate getStringPredicate(Root<User> root, CriteriaBuilder cb, String query, String field) {
        Path<String> titlePath = root.get(field);
        return cb.like(titlePath, "%" + query + "%");
    }
}
