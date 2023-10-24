package com.bevis.exchangedata.specification;

import com.bevis.exchangedata.domain.ExchangeRate;
import com.google.common.base.Strings;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Arrays;

public final class ExchangeRateSpecification {

    private final static String[] SEARCH_FIELDS = new String[]{"currencyCode"};

    public static Specification<ExchangeRate> bySearchQuery(String search) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (Strings.isNullOrEmpty(search)) {
                return criteriaBuilder.conjunction();
            }
            Predicate searchPredicate = getSearchPredicate(root, criteriaBuilder, search);
            return criteriaBuilder.or(searchPredicate);
        };
    }

    private static Predicate getSearchPredicate(Root<ExchangeRate> root, CriteriaBuilder criteriaBuilder, String search) {
        return Arrays.stream(SEARCH_FIELDS)
                .map(fieldName -> getStringPredicate(root, criteriaBuilder, search, fieldName))
                .reduce(criteriaBuilder::or)
                .orElse(criteriaBuilder.conjunction());
    }

    private static Predicate getStringPredicate(Root<ExchangeRate> root, CriteriaBuilder cb, String query, String field) {
        Path<String> titlePath = root.get(field);
        return cb.like(titlePath, "%" + query + "%");
    }
}
