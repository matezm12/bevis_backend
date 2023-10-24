package com.bevis.asset;

import com.bevis.asset.dto.AssetDTO;
import com.bevis.asset.dto.FieldValueDTO;

import java.util.*;
import java.util.stream.Collectors;

import static com.bevis.common.util.CaseFormatUtil.toUnderscore;

public final class FieldsUtil {
    public static Set<String> getFields(List<AssetDTO> content) {
        return content.stream()
                .filter(x -> Objects.nonNull(x.getFields()))
                .flatMap(x -> x.getFields().keySet().stream().filter(y -> !Objects.equals(y, "assetId")))
                .collect(Collectors.toSet());
    }

    public static <T> Map<String, FieldValueDTO> mapFieldValues(Map<String, T> inMap) {
        if (inMap == null)
            return new HashMap<>();
        Map<String, FieldValueDTO> result = new HashMap<>();
        for (Map.Entry<String, T> entry : inMap.entrySet()) {
            String key = entry.getKey();
            result.put(key, FieldValueDTO.of(entry.getValue()));
        }
        return result;
    }

    public static Map<String, Object> mapAssetsDataAttributes(Map<String, FieldValueDTO> fieldValues) {
        Map<String, Object> attrs = new HashMap<>();
        for (Map.Entry<String, FieldValueDTO> e : fieldValues.entrySet()) {
            attrs.put(toUnderscore(e.getKey()), e.getValue().getValue());
        }
        return attrs;
    }

    public static Map<String, String> convertMapValuesToString(Map<String, FieldValueDTO> fieldValues) {
        Map<String, String> attrs = new HashMap<>();
        for (Map.Entry<String, FieldValueDTO> e : fieldValues.entrySet()) {
            attrs.put(e.getKey(), String.valueOf(e.getValue().getValue()));
        }
        return attrs;
    }
}
