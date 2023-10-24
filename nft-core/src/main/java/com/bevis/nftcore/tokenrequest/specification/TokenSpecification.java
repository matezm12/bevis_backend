package com.bevis.nftcore.tokenrequest.specification;

import com.bevis.nftcore.domain.Token;
import com.google.common.base.Strings;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Arrays;

public final class TokenSpecification {
    public static Specification<Token> bySearchRequest(String search) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (Strings.isNullOrEmpty(search)) {
                return criteriaBuilder.conjunction();
            }
            String[] searchFields = new String[]{"name", "tokenId"};
            return Arrays.stream(searchFields)
                    .map(fieldName -> getStringPredicate(root, criteriaBuilder, search, fieldName))
                    .reduce(criteriaBuilder::or)
                    .orElse(criteriaBuilder.conjunction());
        };
    }

    private static Predicate getStringPredicate(Root<Token> root, CriteriaBuilder cb, String query, String field) {
        Path<String> titlePath = root.get(field);
        return cb.like(titlePath, "%" + query + "%");
    }
}
