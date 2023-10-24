package com.bevis.assetimport.specification;

import com.bevis.assetimport.domain.CodeReadrService;
import com.google.common.base.Strings;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Arrays;

import static com.bevis.security.util.SpecificationSecurityUtil.getOwnerUserIdPredicate;

public final class CodeReadrServiceSpecification {
    private final static String[] SEARCH_FIELDS = new String[]{
            "serviceId", "serviceDescription", "assetsTable", "parentAssetKey"
    };

    public static Specification<CodeReadrService> bySearchQuery(String search) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.and(
                getSearchPredicate(root, criteriaBuilder, search),
                getOwnerUserIdPredicate(root, criteriaBuilder, "parentAssetKey")
        );
    }

    private static Predicate getSearchPredicate(Root<CodeReadrService> root, CriteriaBuilder criteriaBuilder, String search) {
        if (Strings.isNullOrEmpty(search)) {
            return criteriaBuilder.conjunction();
        }
        return Arrays.stream(SEARCH_FIELDS)
                .map(fieldName -> getStringPredicate(root, criteriaBuilder, search, fieldName))
                .reduce(criteriaBuilder::or)
                .orElse(criteriaBuilder.conjunction());
    }

    private static Predicate getStringPredicate(Root<CodeReadrService> root, CriteriaBuilder cb, String query, String field) {
        Path<String> titlePath = root.get(field);
        return cb.like(titlePath, "%" + query + "%");
    }
}
