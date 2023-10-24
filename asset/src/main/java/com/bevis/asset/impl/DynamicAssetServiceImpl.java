package com.bevis.asset.impl;

import com.bevis.asset.*;
import com.bevis.asset.dto.AssetDTO;
import com.bevis.asset.dto.AssetRequest;
import com.bevis.asset.dto.FieldValueDTO;
import com.bevis.assettype.AssetTypesService;
import com.bevis.assettype.domain.AssetType;
import com.bevis.common.exception.ObjectNotFoundException;
import com.bevis.common.util.CaseFormatUtil;
import com.bevis.common.util.JsonUtil;
import com.bevis.master.MasterPermissionCheckingService;
import com.bevis.master.MasterService;
import com.bevis.master.PermissionDeniedException;
import com.bevis.master.domain.Asset;
import com.bevis.master.domain.Master;
import com.bevis.master.dto.SearchMasterRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.bevis.asset.FieldSchemaUtil.getFieldsSchema;
import static com.bevis.common.util.CaseFormatUtil.toUnderscore;
import static com.google.common.base.Strings.isNullOrEmpty;

@Transactional
@Slf4j
@Service
@RequiredArgsConstructor
class DynamicAssetServiceImpl implements DynamicAssetService {

    public static final String SKU = "sku";
    public static final String ASSET_SUFFIX = "asset_id";

    private final MasterService masterService;
    private final AssetTypesService assetTypesService;
    private final AssetService assetService;
    private final MasterPermissionCheckingService masterPermissionCheckingService;
    private final DynamicAssetMapper dynamicAssetMapper;
    private final AssetsRelationService assetsRelationService;

    @Override
    public AssetDTO getById(String assetId) {
        Master master = masterService.findById(assetId)
                .orElseThrow(() -> new ObjectNotFoundException("Asset not found"));
        return getByMaster(master);
    }

    @Override
    public AssetDTO getByMaster(Master master) {
        return mapAssetDtoFromMaster(master);
    }

    @Override
    public Optional<AssetDTO> findOne(SearchMasterRequest params) {
        return masterService.findOne(params).map(this::mapAssetDtoFromMaster);
    }

    @Override
    public List<AssetDTO> findAll(SearchMasterRequest params) {
        return masterService.searchMaster(params)
                .stream()
                .map(this::mapAssetDtoFromMaster)
                .collect(Collectors.toList());
    }

    @Override
    public Page<AssetDTO> findAll(SearchMasterRequest params, Pageable pageable) {
        return masterService.searchMaster(params, pageable).map(this::mapAssetDtoFromMaster);
    }

    @Override
    public Map<String, FieldValueDTO> getDynamicFieldsById(String assetId) {
        return getAssetDynamicFields(assetId);
    }

    @Override
    public AssetDTO create(AssetRequest assetRequest) throws DynamicAssetException {
        try {
            final String assetId = assetRequest.getAssetId();
            final Long assetTypeId = assetRequest.getAssetTypeId();
            if (Strings.isBlank(assetId)) {
                return getBlankAsset(assetId);
            }
            masterPermissionCheckingService.validateDynamicAssetEditPermission(assetId);
            final Map<String, FieldValueDTO> fieldValues = dynamicAssetMapper.toUnderscoreFields(assetRequest.getFieldValues());
            updateAssetType(assetId, assetTypeId);
            saveAssetsData(assetRequest);
            createAssetRelations(assetId, fieldValues);
            return AssetDTO.builder()
                    .assetId(assetId)
                    .fields(CaseFormatUtil.makeCamelCaseFields(getAssetDynamicFields(assetId)))
                    .build();
        } catch (PermissionDeniedException e) {
            throw new DynamicAssetException("Error creating asset with cause " + e.getMessage(), e);
        }
    }


    @Override
    public AssetDTO update(AssetRequest assetRequest) throws DynamicAssetException {
        try {
            final String assetId = assetRequest.getAssetId();
            final Long assetTypeId = assetRequest.getAssetTypeId();
            masterPermissionCheckingService.validateDynamicAssetEditPermission(assetId);
            updateAssetType(assetId, assetTypeId);
            saveAssetsData(assetRequest);
            createAssetRelations(assetId, getAssetDynamicFields(assetId));
            return getById(assetId);
        } catch (PermissionDeniedException e) {
            throw new DynamicAssetException("Error updating asset with cause: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteByMaster(Master master) throws DynamicAssetException {
        try {
            final String assetId = master.getId();
            masterPermissionCheckingService.validateDynamicAssetEditPermission(assetId);

            if (assetService.existsById(assetId)) {
                assetService.deleteById(assetId);
            }
            assetsRelationService.deleteByAssetId(assetId);

            master.setFile(null);
            master.setAsset(null);
            master.setIsCsc(null);
            master.setAssetType(null);
            masterService.saveMaster(master);
        } catch (PermissionDeniedException e) {
            throw new DynamicAssetException("Error deleting asset with cause: " + e.getMessage(), e);
        }
    }

    private void updateAssetType(String assetId, Long assetTypeId) {
        if (Objects.nonNull(assetTypeId)) {
            updateAssetType(assetId, getAssetType(assetTypeId));
        }
    }

    private AssetDTO mapAssetDtoFromMaster(Master master) {
        String assetId = master.getId();
        Map<String, FieldValueDTO> fields = getAssetDynamicFields(assetId);
        return AssetDTO.builder()
                .assetId(assetId)
                .master(master)
                .fields(CaseFormatUtil.makeCamelCaseFields(fields))
                .fieldsSchema(getFieldsSchema(master))
                .build();
    }

    private AssetType getAssetType(Long assetTypeId) {
        return assetTypesService.findById(assetTypeId)
                .orElseGet(() -> {
                    AssetType at = new AssetType();
                    at.setId(assetTypeId);
                    return at;
                });
    }

    private void saveAssetsData(AssetRequest assetRequest) {
        Asset asset = assetService.findById(assetRequest.getAssetId())
                .orElseGet(() -> {
                    Asset ad = new Asset();
                    ad.setId(assetRequest.getAssetId());
                    ad.setAttributes(new HashMap<>());
                    return ad;
                });
        Object attributes = asset.getAttributes();
        Map<String, Object> oldAttrs = JsonUtil.mapAttributes(attributes);
        if (Objects.nonNull(assetRequest.getFieldsToRemove()) && !assetRequest.getFieldsToRemove().isEmpty()) {
            oldAttrs.keySet().removeAll(toUnderscore(assetRequest.getFieldsToRemove()));
        }
        Map<String, Object> newAttrs = new HashMap<>(oldAttrs);
        newAttrs.putAll(JsonUtil.mapAttributes(FieldsUtil.mapAssetsDataAttributes(assetRequest.getFieldValues())));
        asset.setAttributes(newAttrs);
        assetService.saveAndFlush(asset);
    }

    private Map<String, FieldValueDTO> getAssetDynamicFields(String assetId) {
        return assetService.findById(assetId)
                .map(Asset::getAttributes)
                .map(FieldsUtil::mapFieldValues)
                .orElse(new HashMap<>());
    }

    private void createAssetRelations(String assetId, Map<String, FieldValueDTO> fieldValues) {
        Set<String> parentAssetIdsSet = fieldValues.entrySet()
                .stream()
                .filter(x -> x.getKey().endsWith(ASSET_SUFFIX) || Objects.equals(x.getKey(), SKU))
                .map(Map.Entry::getValue)
                .map(FieldValueDTO::getValue)
                .filter(Objects::nonNull)
                .map(Object::toString)
                .filter(x -> !isNullOrEmpty(x))
                .collect(Collectors.toSet());

        List<String> parentAssetIds = parentAssetIdsSet.stream()
                .filter(x -> !assetsRelationService.exists(assetId, x))
                .collect(Collectors.toList());
        if (!parentAssetIds.isEmpty()) {
            assetsRelationService.saveRelations(assetId, parentAssetIds);
        }

        List<String> relationsToRemove = assetsRelationService.findParentsOfAssetId(assetId).stream()
                .map(Master::getId)
                .filter(x -> !parentAssetIdsSet.contains(x))
                .collect(Collectors.toList());
        if (!relationsToRemove.isEmpty()) {
            assetsRelationService.deleteRelationsByAssetId(assetId, relationsToRemove);
        }
    }

    private void updateAssetType(String assetId, AssetType assetType) {
        masterService.findById(assetId)
                .ifPresent(master -> {
                    master.setAssetType(assetType);
                    masterService.saveMaster(master);
                });
    }

    private AssetDTO getBlankAsset(String assetId) {
        return AssetDTO.builder()
                .assetId(assetId)
                .fields(new HashMap<>())
                .build();
    }

}
