package com.bevis.asset;

import com.bevis.assettype.domain.AssetType;
import com.bevis.master.domain.Master;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public final class FieldSchemaUtil {
    public static Map<String, Object> getFieldsSchema(Master master) {
        return Optional.of(master)
                .map(Master::getAssetType)
                .map(AssetType::getFieldsSchema)
                .orElse(null);
    }

    public static Optional<String> getTitleByFieldName(Map<String, Object> schema, String fieldName) {
        final String FIELDS = "fields";
        final String FIELD_TITLE = "title";
        final String FIELD_NAME = "name";
        try {
            List<Map<String, Object>> fieldsList = (List<Map<String, Object>>) schema.getOrDefault(FIELDS, Collections.emptyList());
            return fieldsList.stream()
                    .filter(x -> Objects.equals(x.get(FIELD_NAME), fieldName))
                    .findFirst()
                    .map(x -> String.valueOf(x.getOrDefault(FIELD_TITLE, StringUtils.capitalize(fieldName))));
        } catch (Exception e){
            return Optional.empty();
        }
    }

}
