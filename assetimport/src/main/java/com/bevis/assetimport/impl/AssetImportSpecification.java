package com.bevis.assetimport.impl;

import com.bevis.assetimport.domain.AssetImport;
import com.bevis.assetimport.dto.AssetImportFilter;
import com.google.common.base.Strings;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.bevis.security.util.SpecificationSecurityUtil.getOwnerUserIdPredicate;

public final class AssetImportSpecification {

    private final static String[] SEARCH_FIELDS = new String[]{"scanId", "codereadrBody", "barcode"};

    public static Specification<AssetImport> bySearchQuery(AssetImportFilter filter) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.and(
                getFullSearchPredicate(root, criteriaBuilder, filter.getSearch()),
                getMatchedPredicate(root, criteriaBuilder, filter.getOnlyUnprocessed()),
                getServiceIdPredicate(root, criteriaBuilder, filter.getServiceId()),
                getOwnerUserIdPredicate(root, criteriaBuilder, "vendorAssetId")
        );
    }

    private static Predicate getMatchedPredicate(Root<AssetImport> root, CriteriaBuilder criteriaBuilder, Boolean onlyUnprocessed) {
        if (Objects.isNull(onlyUnprocessed) || !onlyUnprocessed) {
            return criteriaBuilder.conjunction();
        }
        Path<Boolean> matchedPath = root.get("matched");
        return criteriaBuilder.equal(matchedPath, false);
    }

    private static Predicate getServiceIdPredicate(Root<AssetImport> root, CriteriaBuilder criteriaBuilder, String serviceId) {
        if (Objects.isNull(serviceId)) {
            return criteriaBuilder.conjunction();
        }
        Path<String> serviceIdPath = root.get("serviceId");
        return criteriaBuilder.equal(serviceIdPath, serviceId);
    }

    private static Predicate getFullSearchPredicate(Root<AssetImport> root, CriteriaBuilder criteriaBuilder, String search) {
        if (Strings.isNullOrEmpty(search)) {
            return criteriaBuilder.conjunction();
        }
        Predicate idPredicate = getIdPredicate(root, criteriaBuilder, search);
        Predicate searchPredicate = getSearchPredicate(root, criteriaBuilder, search);
        return criteriaBuilder.or(idPredicate, searchPredicate);
    }

    private static Predicate getIdPredicate(Root<AssetImport> root, CriteriaBuilder criteriaBuilder, String search) {
        Pattern compile = Pattern.compile("^[1-9][0-9]{0,6}$");
        Matcher matcher = compile.matcher(search);
        if (matcher.matches()) {
            Path<Long> idPath = root.get("id");
            long searchedId = Long.parseLong(search);
            return criteriaBuilder.equal(idPath, searchedId);
        }
        return criteriaBuilder.disjunction();
    }

    private static Predicate getSearchPredicate(Root<AssetImport> root, CriteriaBuilder criteriaBuilder, String search) {
        return Arrays.stream(SEARCH_FIELDS)
                .map(fieldName -> getStringPredicate(root, criteriaBuilder, search, fieldName))
                .reduce(criteriaBuilder::or)
                .orElse(criteriaBuilder.conjunction());
    }

    private static Predicate getStringPredicate(Root<AssetImport> root, CriteriaBuilder cb, String query, String field) {
        Path<String> titlePath = root.get(field);
        return cb.like(titlePath, "%" + query + "%");
    }
}
