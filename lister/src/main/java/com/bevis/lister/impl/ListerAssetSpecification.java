package com.bevis.lister.impl;

import com.bevis.user.domain.User;
import com.bevis.lister.ListerAsset;
import com.google.common.base.Strings;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Arrays;

public final class ListerAssetSpecification {
    private final static String[] SEARCH_FIELDS = new String[]{ "name" };

    public static Specification<ListerAsset> bySearchQuery(String search, User user) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            Predicate userPredicate = criteriaBuilder.equal(root.get("user"), user);
            if (Strings.isNullOrEmpty(search)) {
                return userPredicate;
            }
            return criteriaBuilder.and(userPredicate, getSearchPredicate(root, criteriaBuilder, search));
        };
    }

    private static Predicate getSearchPredicate(Root<ListerAsset> root, CriteriaBuilder criteriaBuilder, String search) {
        return Arrays.stream(SEARCH_FIELDS)
                .map(fieldName -> getStringPredicate(root, criteriaBuilder, search, fieldName))
                .reduce(criteriaBuilder::or)
                .orElse(criteriaBuilder.conjunction());
    }

    private static Predicate getStringPredicate(Root<ListerAsset> root, CriteriaBuilder cb, String query, String field) {
        Path<String> titlePath = root.get(field);
        return cb.like(titlePath, "%" + query + "%");
    }
}
