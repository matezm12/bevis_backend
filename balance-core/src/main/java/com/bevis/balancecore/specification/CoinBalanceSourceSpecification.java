package com.bevis.balancecore.specification;

import com.bevis.balancecore.domain.CoinBalanceSource;
import com.google.common.base.Strings;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Arrays;

public final class CoinBalanceSourceSpecification {
    private final static String[] SEARCH_FIELDS = new String[]{
            "currency", "sourceKey", "description"
    };

    public static Specification<CoinBalanceSource> bySearchQuery(String search) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (Strings.isNullOrEmpty(search)) {
                return criteriaBuilder.conjunction();
            }
            return getSearchPredicate(root, criteriaBuilder, search);
        };
    }

    private static Predicate getSearchPredicate(Root<CoinBalanceSource> root, CriteriaBuilder criteriaBuilder, String search) {
        return Arrays.stream(SEARCH_FIELDS)
                .map(fieldName -> getStringPredicate(root, criteriaBuilder, search, fieldName))
                .reduce(criteriaBuilder::or)
                .orElse(criteriaBuilder.conjunction());
    }

    private static Predicate getStringPredicate(Root<CoinBalanceSource> root, CriteriaBuilder cb, String query, String field) {
        Path<String> titlePath = root.get(field);
        return cb.like(titlePath, "%" + query + "%");
    }
}
