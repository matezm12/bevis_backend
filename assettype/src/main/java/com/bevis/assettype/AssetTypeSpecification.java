package com.bevis.assettype;

import com.bevis.assettype.domain.AssetType;
import com.google.common.base.Strings;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Arrays;

public final class AssetTypeSpecification {
    private final static String[] SEARCH_FIELDS = new String[]{ "name", "key" };

    public static Specification<AssetType> bySearchQuery(String search) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            Predicate notDeleted = criteriaBuilder.equal(root.get("deleted"), false);
            if (Strings.isNullOrEmpty(search)) {
                return notDeleted;
            }
            return criteriaBuilder.and(notDeleted, getSearchPredicate(root, criteriaBuilder, search));
        };
    }

    private static Predicate getSearchPredicate(Root<AssetType> root, CriteriaBuilder criteriaBuilder, String search) {
        return Arrays.stream(SEARCH_FIELDS)
                .map(fieldName -> getStringPredicate(root, criteriaBuilder, search, fieldName))
                .reduce(criteriaBuilder::or)
                .orElse(criteriaBuilder.conjunction());
    }

    private static Predicate getStringPredicate(Root<AssetType> root, CriteriaBuilder cb, String query, String field) {
        Path<String> titlePath = root.get(field);
        return cb.like(titlePath, "%" + query + "%");
    }

}
