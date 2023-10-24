package com.bevis.assetinfo.impl;

import com.bevis.asset.DynamicAssetService;
import com.bevis.asset.dto.AssetDTO;
import com.bevis.asset.dto.FieldValueDTO;
import com.bevis.assetinfo.AssetGroupsInfoService;
import com.bevis.assetinfo.LogoFetchingService;
import com.bevis.assetinfo.dto.AssetGroup;
import com.bevis.assetinfo.dto.AssetGroupsInfo;
import com.bevis.assetinfo.dto.AssetValue;
import com.bevis.assetinfo.dto.AssetValuesWrapper;
import com.bevis.assettype.domain.AssetType;
import com.bevis.blockchaincore.BlockchainUrlGateway;
import com.bevis.blockchaincore.domain.Blockchain;
import com.bevis.common.exception.ObjectNotFoundException;
import com.bevis.gateway.UrlGatewayService;
import com.bevis.master.MasterService;
import com.bevis.master.domain.Master;
import com.bevis.master.domain.MasterImport;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.bevis.asset.FieldSchemaUtil.getTitleByFieldName;
import static com.bevis.assetinfo.util.RegexUtil.*;
import static com.bevis.assetinfo.util.StringCastUtil.mapUnderscore;
import static com.bevis.common.util.CaseFormatUtil.toCamelCase;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
class AssetGroupsInfoServiceImpl implements AssetGroupsInfoService {

    private final static String GPS_FIELD = "gps";
    private final static String CARRIER = "carrier";
    private final static String TRACKING_NUMBER = "trackingNumber";

    private final MasterService masterService;
    private final UrlGatewayService urlGatewayService;
    private final BlockchainUrlGateway blockchainUrlGateway;
    private final DynamicAssetService dynamicAssetService;
    private final LogoFetchingService logoFetchingService;

    @Override
    public AssetGroupsInfo getAssetInfo(String assetId) {
        Master master = masterService.findByIdOrPublicKey(assetId)
                .orElseThrow(() -> new ObjectNotFoundException("Asset not found"));
        return getAssetInfo(master);
    }

    @Override
    public AssetGroupsInfo getAssetInfo(Master master) {

        String blockchain = Optional.of(master)
                .map(Master::getBlockchain)
                .map(Blockchain::getName)
                .orElse("");

        Optional<MasterImport> masterImportOpt = Optional.of(master)
                .map(Master::getMasterImport);

        return AssetGroupsInfo.builder()
                .assetId(master.getId())
                .blockchain(blockchain)
                .publicKey(master.getPublicKey())
                .logo(logoFetchingService.loadFileUrlByMaster(master))
                .publicKeyUrl(getBlockchainAddressLink(master))
                .genDate(master.getGenDate())
                .assetType(getAssetType(master))
                .codeReadrScanId(master.getCodereadrScanId())
                .masterImportId(masterImportOpt.map(MasterImport::getId).orElse(null))
                .masterImportName(masterImportOpt.map(MasterImport::getName).orElse(null))
                .assetGroups(loadAssetGroups(master))
                .build();
    }

    private String getAssetType(Master master) {
        return Optional.of(master)
                .map(Master::getAssetType)
                .map(AssetType::getName)
                .orElse(null);
    }

    private String getBlockchainAddressLink(@NonNull Master master) {
        Blockchain blockchain = master.getBlockchain();
        if (Objects.isNull(blockchain)) {
            log.warn("Blockchain  is null for PublicKey: {}", master.getPublicKey());
            return null;
        }
        return blockchainUrlGateway.getBlockchainAddressLink(master.getPublicKey(), blockchain);
    }

    @Override
    public List<AssetGroup> loadAssetGroups(Master master) {
        List<AssetGroup> assetGroups = new ArrayList<>();

        Set<String> usedAssetIds = new HashSet<>();
        Map<String, String> allAssets = new LinkedHashMap<>();
        Queue<String> assetsQueue = new LinkedList<>();

        assetsQueue.add(master.getId());
        do {
            String assetId = assetsQueue.poll();
            if (!usedAssetIds.contains(assetId) && masterService.exists(assetId)) {
                AssetValuesWrapper assetValuesWrapper = loadAssetValues(assetId);

                assetGroups.add(AssetGroup.builder()
                        .groupKey(toCamelCase(allAssets.getOrDefault(assetId, "asset")))
                        .groupName(mapGroupName(allAssets.getOrDefault(assetId, "asset")))
                        .groupAssetId(assetId)
                        .assetValues(assetValuesWrapper.getAssetValues())
                        .build());

                usedAssetIds.add(assetId);

                Map<String, String> relatedAssets = assetValuesWrapper.getRelatedAssets();
                relatedAssets.entrySet()
                        .stream()
                        .filter(x -> !usedAssetIds.contains(x.getKey()))
                        .forEach(x -> {
                            assetsQueue.add(x.getKey());
                            allAssets.put(x.getKey(), x.getValue());
                        });

            }
        } while (!assetsQueue.isEmpty());

        return assetGroups;
    }

    private AssetValuesWrapper loadAssetValues(String assetId) {
        AssetDTO assetDTO = dynamicAssetService.getById(assetId);
        List<AssetValue> assetValues = mapAssetValues(assetDTO);
        mapMissedLinks(assetValues);
        return AssetValuesWrapper.builder()
                .assetValues(assetValues)
                .relatedAssets(getRelatedAssets(assetDTO))
                .build();
    }

    private void mapMissedLinks(List<AssetValue> assetValues) {
        findValueByKey(assetValues, CARRIER)
                .ifPresent(carrier -> findFieldValueByKey(assetValues, TRACKING_NUMBER)
                        .ifPresent(trackingNumberField -> {
                            String trackingNumber = Optional.ofNullable(trackingNumberField.getFieldValue()).map(String::valueOf)
                                    .orElse("");
                            trackingNumberField.setLink(urlGatewayService.getCarrierTrackingLink(carrier, trackingNumber));
                        }));
        assetValues.stream()
                .filter(x -> Objects.isNull(x.getLink()))
                .filter(x -> validateLink(x.getFieldValue()))
                .forEach(x -> x.setLink(x.getFieldValue()));
    }

    private Optional<String> findValueByKey(List<AssetValue> assetValues, String key) {
        return findFieldValueByKey(assetValues, key)
                .map(AssetValue::getFieldValue)
                .map(String::valueOf);
    }

    private Optional<AssetValue> findFieldValueByKey(List<AssetValue> assetValues, String key) {
        if (Objects.isNull(assetValues) || assetValues.isEmpty()) {
            return Optional.empty();
        }
        return assetValues.stream()
                .filter(x -> Objects.equals(x.getFieldName(), key))
                .findFirst();
    }

    private Map<String, String> getRelatedAssets(AssetDTO assetDTO) {
        if (Objects.isNull(assetDTO)) {
            return Collections.emptyMap();
        }
        Map<String, FieldValueDTO> assetDynamicFields = assetDTO.getFields();
        Set<Map.Entry<String, FieldValueDTO>> entries = assetDynamicFields.entrySet();
        Map<String, String> relatedAssets = new LinkedHashMap<>();

        entries.stream()
                .filter(entry -> Objects.nonNull(entry.getValue()) && Objects.nonNull(entry.getValue().getValue()))
                .filter(entry -> isFieldAsset(entry.getKey(), String.valueOf(entry.getValue().getValue())))
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> relatedAssets.put(String.valueOf(entry.getValue().getValue()), entry.getKey()));

        return relatedAssets;
    }

    private boolean isFieldAsset(String fieldName, String fieldValue) {
        boolean skuAsset = isSkuField(fieldName) && isAssetId(fieldValue);
        boolean assetField = isAssetField(fieldName);
        return assetField || skuAsset;
    }

    private List<AssetValue> mapAssetValues(AssetDTO assetDTO) {
        if (Objects.isNull(assetDTO) || Objects.isNull(assetDTO.getFields())) {
            return Collections.emptyList();
        }
        Map<String, FieldValueDTO> assetDynamicFields = assetDTO.getFields();
        Set<Map.Entry<String, FieldValueDTO>> entries = assetDynamicFields.entrySet();
        return entries.stream()
                .filter(entry -> Objects.nonNull(entry.getValue()))
                .map(entry -> mapAssetValue(entry.getKey(), entry.getValue(), assetDTO.getFieldsSchema()))
                .filter(assetValue -> Strings.isNotBlank(assetValue.getFieldValue()))
                .sorted(Comparator.comparing(AssetValue::getFieldName))
                .collect(Collectors.toList());
    }

    private AssetValue mapAssetValue(String fieldKey, FieldValueDTO fieldValueDTO, Map<String, Object> fieldsSchema) {
        Optional<FieldValueDTO> opt = Optional.of(fieldValueDTO);
        String fieldValue = opt.map(FieldValueDTO::getValue)
                .map(String::valueOf)
                .orElse("");
        boolean fieldAsset = isFieldAsset(fieldKey, fieldValue);
        return AssetValue.builder()
                .fieldName(fieldKey)
                .fieldTitle(getTitleByFieldName(fieldsSchema, fieldKey).orElse(mapUnderscore(fieldKey)))
                .fieldValue(fieldValue)
                .isAsset(fieldAsset)
                .link(fieldAsset ? urlGatewayService.getAssetViewerLink(fieldValue) : getGpsLink(fieldKey, fieldValue))
                .build();
    }

    private String getGpsLink(String fieldKey, String fieldValue) {
        if (Objects.equals(fieldKey, GPS_FIELD)) {
            return urlGatewayService.getGoogleMapLink(fieldValue);
        }
        return null;
    }

}
