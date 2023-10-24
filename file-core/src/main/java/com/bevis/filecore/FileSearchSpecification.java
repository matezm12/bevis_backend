package com.bevis.filecore;

import com.bevis.filecore.domain.File;
import com.bevis.filecore.dto.FileFilter;
import org.apache.logging.log4j.util.Strings;
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
import static com.bevis.filecore.util.ReflectionUtil.getFieldValue;

final class FileSearchSpecification {

    private final static String[] SEARCH_FIELDS = new String[]{"ipfs", "transactionId", "assetId"};
    private final static String[] FILTER_FIELDS = new String[]{"fileType", "state", "blockchain"};

    public static Specification<File> bySearchQuery(FileFilter filter) {

        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.and(
                getSearchAllPredicate(root, criteriaBuilder, filter.getSearch()),
                getOnlyEncryptedPredicate(root, criteriaBuilder, filter.getOnlyEncrypted()),
                Arrays.stream(FILTER_FIELDS)
                        .map(fieldName -> getSimplePredicate(root, criteriaBuilder, getFieldValue(filter, fieldName), fieldName))
                        .reduce(criteriaBuilder.conjunction(), criteriaBuilder::and),
                getOwnerUserIdPredicate(root, criteriaBuilder, "ownerAssetId")
        );
    }

    private static <T> Predicate getSimplePredicate(Root<File> root, CriteriaBuilder criteriaBuilder, T value, String fieldName) {
        if (Objects.isNull(value)) {
            return criteriaBuilder.conjunction();
        }
        Path<T> fileTypePath = root.get(fieldName);
        return criteriaBuilder.equal(fileTypePath, value);
    }

    private static Predicate getOnlyEncryptedPredicate(Root<File> root, CriteriaBuilder criteriaBuilder, Boolean onlyEncrypted) {
        if (Objects.isNull(onlyEncrypted) || !onlyEncrypted) {
            return criteriaBuilder.conjunction();
        }
        Path<Boolean> fileTypePath = root.get("encrypted");
        return criteriaBuilder.equal(fileTypePath, true);
    }

    private static Predicate getSearchAllPredicate(Root<File> root, CriteriaBuilder criteriaBuilder, String search) {
        if (Strings.isBlank(search)) {
            return criteriaBuilder.conjunction();
        }
        Predicate idPredicate = getIdPredicate(root, criteriaBuilder, search);
        Predicate searchPredicate = getSearchPredicate(root, criteriaBuilder, search);
        return criteriaBuilder.or(idPredicate, searchPredicate);
    }

    private static Predicate getIdPredicate(Root<File> root, CriteriaBuilder criteriaBuilder, String search) {
        Pattern compile = Pattern.compile("^[1-9][0-9]{0,6}$");
        Matcher matcher = compile.matcher(search);
        if (matcher.matches()) {
            Path<Long> idPath = root.get("id");
            long searchedId = Long.parseLong(search);
            return criteriaBuilder.equal(idPath, searchedId);
        }
        return criteriaBuilder.disjunction();
    }

    private static Predicate getSearchPredicate(Root<File> root, CriteriaBuilder criteriaBuilder, String search) {
        return Arrays.stream(SEARCH_FIELDS)
                .map(fieldName -> getStringPredicate(root, criteriaBuilder, search, fieldName))
                .reduce(criteriaBuilder::or)
                .orElse(criteriaBuilder.conjunction());
    }

    private static Predicate getStringPredicate(Root<File> root, CriteriaBuilder cb, String query, String field) {
        Path<String> titlePath = root.get(field);
        return cb.like(titlePath, "%" + query + "%");
    }
}
