package com.bevis.master.specification;

import com.bevis.assettype.domain.AssetType;
import com.bevis.blockchaincore.domain.Blockchain;
import com.bevis.master.domain.Master;
import com.bevis.master.domain.MasterImport;
import com.bevis.master.dto.SearchMasterRequest;
import com.bevis.master.domain.Asset;
import com.google.common.base.Strings;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

import static com.bevis.common.util.CaseFormatUtil.toUnderscore;
import static com.bevis.security.util.SpecificationSecurityUtil.getOwnerUserIdPredicate;

public final class MasterSpecification {

    public static Specification<Master> byAnonymousSearchMasterRequest(SearchMasterRequest searchMasterRequest) {
        return (root, criteriaQuery, criteriaBuilder) -> getSearchablePredicate(searchMasterRequest, root, criteriaBuilder);
    }

    public static Specification<Master> bySearchMasterRequest(SearchMasterRequest searchMasterRequest) {
        return bySearchMasterRequest(searchMasterRequest, null);
    }

    public static Specification<Master> bySearchMasterRequest(SearchMasterRequest searchMasterRequest, Sort sort) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (Objects.nonNull(sort)) {
                MasterQueryUtils.sort(root, criteriaQuery, criteriaBuilder, sort);
            }
            return criteriaBuilder.and(
                    getSearchablePredicate(searchMasterRequest, root, criteriaBuilder),
                    getOwnerUserIdPredicate(root, criteriaBuilder, "ownerAssetId")
            );
        };
    }

    private static Predicate getSearchablePredicate(SearchMasterRequest searchMasterRequest, Root<Master> root, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.and(
                getSearchPredicate(root, criteriaBuilder, searchMasterRequest.getSearch()),
                getAssetTypePredicate(root, criteriaBuilder, searchMasterRequest),
                getBlockchainIdPredicate(root, criteriaBuilder, searchMasterRequest.getBlockchainId()),
                getMasterImportPredicate(root, criteriaBuilder, searchMasterRequest.getMasterImportId()),
                getCodereadrScanIdPredicate(root, criteriaBuilder, searchMasterRequest.getCodereadrScanId()),
                getShowInactivePredicate(root, criteriaBuilder, searchMasterRequest.getShowInactive()),
                getDynamicFilterPredicate(root, criteriaBuilder, searchMasterRequest.getDynamicFilter())
        );
    }

    private static Predicate getSearchPredicate(Root<Master> root, CriteriaBuilder criteriaBuilder, String search) {
        if (Strings.isNullOrEmpty(search)) {
            return criteriaBuilder.conjunction();
        }
        Predicate searchByStaticFields = getSearchByStaticFields(root, criteriaBuilder, search);
        Predicate searchByDynamicFields = getSearchByDynamicFields(root, criteriaBuilder, search);
        return criteriaBuilder.or(searchByStaticFields, searchByDynamicFields);

    }

    private static Predicate getSearchByStaticFields(Root<Master> root, CriteriaBuilder criteriaBuilder, String search) {
        String[] searchFields = new String[]{"id", "publicKey", "codereadrScanId"};
        return Arrays.stream(searchFields)
                .map(fieldName -> getStringPredicate(root, criteriaBuilder, search, fieldName))
                .reduce(criteriaBuilder::or)
                .orElse(criteriaBuilder.conjunction());
    }

    private static Predicate getSearchByDynamicFields(Root<Master> root, CriteriaBuilder criteriaBuilder, String search) {
        Join<Master, Asset> assetData = root.join("asset", JoinType.LEFT);
        Path<Map<String, Object>> attributesPath = assetData.get("attributes");
        String s = "%" + search.toUpperCase() + "%";
        Expression<String> jsonExtracted = criteriaBuilder.function("JSON_EXTRACT", String.class, attributesPath,
                criteriaBuilder.literal("$"));
        Expression<String> toUpperCase = criteriaBuilder.function("UPPER", String.class, jsonExtracted);
        return criteriaBuilder.like(toUpperCase, s);
    }

    private static Predicate getAssetTypePredicate(Root<Master> root, CriteriaBuilder criteriaBuilder, SearchMasterRequest searchMasterRequest) {
        Long assetTypeId = searchMasterRequest.getAssetTypeId();

        Join<Master, AssetType> assetType = root.join("assetType", JoinType.LEFT);

        Predicate assetTypeIdPredicate = criteriaBuilder.conjunction();
        if (Objects.nonNull(assetTypeId)) {
            Path<Long> id = assetType.get("id");
            assetTypeIdPredicate = criteriaBuilder.equal(id, assetTypeId);
        }

        Predicate productsOnlyPredicate = criteriaBuilder.conjunction();
        if (Boolean.TRUE.equals(searchMasterRequest.getProductsOnly())) {
            Path<Boolean> isProduct = assetType.get("isProduct");
            productsOnlyPredicate = criteriaBuilder.equal(isProduct, searchMasterRequest.getProductsOnly());
        }

        return criteriaBuilder.and(productsOnlyPredicate, assetTypeIdPredicate);
    }

    private static Predicate getMasterImportPredicate(Root<Master> root, CriteriaBuilder criteriaBuilder, Long masterImportId) {
        if (Objects.isNull(masterImportId)) {
            return criteriaBuilder.conjunction();
        }
        Join<Master, MasterImport> masterImport = root.join("masterImport");
        Path<Long> id = masterImport.get("id");
        return criteriaBuilder.equal(id, masterImportId);
    }

    private static Predicate getBlockchainIdPredicate(Root<Master> root, CriteriaBuilder criteriaBuilder, Long blockchainId) {
        if (Objects.isNull(blockchainId)) {
            return criteriaBuilder.conjunction();
        }
        Join<Master, Blockchain> blockchain = root.join("blockchain");
        Path<Long> id = blockchain.get("id");
        return criteriaBuilder.equal(id, blockchainId);
    }

    private static Predicate getShowInactivePredicate(Root<Master> root, CriteriaBuilder criteriaBuilder, Boolean showInactive) {
        if (Objects.isNull(showInactive) || !showInactive) {
            Path<Boolean> isActivePath = root.get("isActive");
            return criteriaBuilder.equal(isActivePath, true);
        }
        return criteriaBuilder.conjunction();
    }

    private static Predicate getCodereadrScanIdPredicate(Root<Master> root, CriteriaBuilder criteriaBuilder, String codereadrScanId) {
        if (Objects.isNull(codereadrScanId)) {
            return criteriaBuilder.conjunction();
        }
        Path<String> codeReadrScanIdPath = root.get("codereadrScanId");
        return criteriaBuilder.equal(codeReadrScanIdPath, codereadrScanId);
    }

    private static Predicate getDynamicFilterPredicate(Root<Master> root, CriteriaBuilder criteriaBuilder, Map<String, String> dynamicFilter) {
        if (Objects.isNull(dynamicFilter) || dynamicFilter.isEmpty()) {
            return criteriaBuilder.conjunction();
        }
        Join<Master, Asset> assetData = root.join("asset", JoinType.LEFT);
        Path<Map<String, Object>> attributesPath = assetData.get("attributes");

        return dynamicFilter.keySet().stream()
                .map(attributeName -> {
                    Expression<String> jsonExtracted = criteriaBuilder.function("JSON_EXTRACT", String.class, attributesPath,
                            criteriaBuilder.literal("$." + toUnderscore(attributeName)));
                    return criteriaBuilder.equal(jsonExtracted, dynamicFilter.get(attributeName));
                }).reduce(criteriaBuilder::and)
                .orElse(criteriaBuilder.conjunction());
    }

    private static Predicate getStringPredicate(Root<Master> root, CriteriaBuilder cb, String query, String field) {
        Path<String> titlePath = root.get(field);
        return cb.like(titlePath, "%" + query + "%");
    }

}
