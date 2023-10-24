package com.bevis.assetimport.mapper;

import com.bevis.assetimport.dto.AssetImportDTO;
import com.bevis.assetimport.dto.ImportWrappingDTO;
import com.bevis.assetimport.dto.ProductDTO;
import com.bevis.master.domain.Master;
import com.bevis.asset.dto.FieldValueDTO;
import com.bevis.master.MasterService;
import com.google.common.base.Strings;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.bevis.common.util.DateUtil.convertInstantToLocalDate;

@Component
@RequiredArgsConstructor
public class CodeReadrQuestionsMapper {

    private static final String FIELD_SUFFIX_ASSET = "_asset_id";
    private static final  String PRODUCT_UPC = "product_upc";

    private final MasterService masterService;

    public Map<String, FieldValueDTO> getDynamicQuestionFieldValues(AssetImportDTO dynamicAssetImport,
                                                                    Map<String, String> fieldNames,
                                                                    Map<String, Function<String, String>> convertors) {
        Map<String, String> formatterQuestionArgs = dynamicAssetImport.getFormatterQuestionArgs();
        Map<String, FieldValueDTO> assetsFieldValues = formatterQuestionArgs.entrySet()
                .stream().filter(x -> x.getKey().endsWith(FIELD_SUFFIX_ASSET))
                .filter(x -> Objects.nonNull(x.getValue()) && !Strings.isNullOrEmpty(x.getValue().trim()))
                .collect(Collectors.toMap(
                        x -> mapDynamicFieldName(x.getKey(), fieldNames),
                        y -> new FieldValueDTO(getAssetId(y.getValue()))
                ));

        Map<String, FieldValueDTO> nonAssetsFieldValues = formatterQuestionArgs.entrySet()
                .stream()
                .filter(x -> Objects.nonNull(x.getValue()) && !Strings.isNullOrEmpty(x.getValue().trim()))
                .filter(x -> !x.getKey().endsWith(FIELD_SUFFIX_ASSET))
                .filter(x -> !isSkippedField(x.getKey()))
                .collect(Collectors.toMap(
                        x -> mapDynamicFieldName(x.getKey(), fieldNames),
                        y -> new FieldValueDTO(mapDynamicFieldValue(y.getKey(), y.getValue(), convertors))
                ));

        Map<String, FieldValueDTO> result = new HashMap<>(assetsFieldValues);
        result.putAll(nonAssetsFieldValues);
        return result;
    }

    public Map<String, FieldValueDTO> getStaticFieldValues(ImportWrappingDTO assetImportDTO) {
        AssetImportDTO dynamicAssetImport = assetImportDTO.getDynamicAssetImport();
        Map<String, FieldValueDTO> fieldValues = new HashMap<>();
        if (Objects.nonNull(dynamicAssetImport.getDeviceId())) {
            fieldValues.put("qc_id", new FieldValueDTO(dynamicAssetImport.getDeviceId().toString()));
        }
        if (Objects.nonNull(dynamicAssetImport.getScanTime())) {
            LocalDate datePack = convertInstantToLocalDate(dynamicAssetImport.getScanTime());
            if (Objects.nonNull(datePack)) {
                fieldValues.put("date_pack", new FieldValueDTO(datePack.toString()));
            }
        }
        if (Objects.nonNull(dynamicAssetImport.getScanId())) {
            fieldValues.put("import_id", new FieldValueDTO(dynamicAssetImport.getScanId()));
        }
        ProductDTO productDTO = assetImportDTO.getProductDTO();
        if (Objects.nonNull(productDTO)) {
            fieldValues.put("sku", new FieldValueDTO(productDTO.getAssetId()));
        }
        String vendorAssetId = assetImportDTO.getVendorAssetId();
        if (Objects.nonNull(vendorAssetId)) {
            fieldValues.put("vendor_asset_id", new FieldValueDTO(vendorAssetId));
        }
        String operatorAssetId = assetImportDTO.getOperatorAssetId();
        if (Objects.nonNull(operatorAssetId)) {
            fieldValues.put("qc_asset_id", new FieldValueDTO(operatorAssetId));
        }
        return fieldValues;
    }

    public String getAssetId(String assetIdOrPublicKey) {
        if (assetIdOrPublicKey.length() == 6) {
            return assetIdOrPublicKey;
        }
        return masterService.findFirstByPublicKeyOrId(assetIdOrPublicKey, assetIdOrPublicKey)
                .map(Master::getId).orElse("");
    }

    private String mapDynamicFieldName(String name, Map<String, String> fieldNamesMap) {
        return fieldNamesMap.getOrDefault(name, name);
    }

    private String mapDynamicFieldValue(String name, String value, Map<String, Function<String, String>> convertors) {
        return convertors.containsKey(name) ? convertors.get(name).apply(value) : value;
    }

    private boolean isSkippedField(String key) {
        return PRODUCT_UPC.equals(key);
    }

}
