package com.bevis.blockchaincore;

import com.bevis.blockchaincore.domain.Blockchain;
import com.google.common.base.Strings;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Arrays;

public final class BlockchainSpecification {
    private final static String[] SEARCH_FIELDS = new String[]{
            "name", "currencyCode", "fullName",
            "nodeAddress", "explorerUrl", "addressUrl",
            "assetIdRegex", "balanceSource", "jobBalanceSource"
    };

    public static Specification<Blockchain> bySearchQuery(String search) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (Strings.isNullOrEmpty(search)) {
                return criteriaBuilder.conjunction();
            }
            return getSearchPredicate(root, criteriaBuilder, search);
        };
    }

    private static Predicate getSearchPredicate(Root<Blockchain> root, CriteriaBuilder criteriaBuilder, String search) {
        return Arrays.stream(SEARCH_FIELDS)
                .map(fieldName -> getStringPredicate(root, criteriaBuilder, search, fieldName))
                .reduce(criteriaBuilder::or)
                .orElse(criteriaBuilder.conjunction());
    }

    private static Predicate getStringPredicate(Root<Blockchain> root, CriteriaBuilder cb, String query, String field) {
        Path<String> titlePath = root.get(field);
        return cb.like(titlePath, "%" + query + "%");
    }
}
