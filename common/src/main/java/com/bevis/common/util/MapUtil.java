package com.bevis.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.bevis.common.util.CaseFormatUtil.toCamelCase;
import static com.bevis.common.util.CaseFormatUtil.toUnderscore;

public final class MapUtil {
    public static Map<String, Object> convertObjectToMap(Object o) {
        ObjectMapper oMapper = new ObjectMapper();
        return oMapper.convertValue(o, Map.class);
    }

    public static Map<String, Object> convertJsonStringToMap(String json){
        ObjectMapper mapper = new ObjectMapper();
        try {
            // convert JSON string to Map
            return mapper.readValue(json, Map.class);
        } catch (IOException e) {
            return new HashMap<>();
        }
    }

    @NotNull
    public static Map<String, Object> filterMap(Map<String, Object> formData, Collection<String> keysToRemove) {
        Set<String> keysTORemoveSet = new HashSet<>(keysToRemove);
        return formData.entrySet()
                .stream()
                .filter(x -> !keysTORemoveSet.contains(x.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static <V> void renameMapKey(Map<String, V> map, String oldKey, String newKey) {
        V obj = map.remove(oldKey);
        map.put(newKey, obj);
    }

    public static <T> Map<String, T> toUnderscoreMapKeys(Map<String, T> formData) {
        return formData.entrySet()
                .stream()
                .collect(Collectors.toMap(k -> toUnderscore(k.getKey()), Map.Entry::getValue));
    }

    public static <T> Map<String, T> toCamelcaseMapKeys(Map<String, T> formData) {
        return formData.entrySet()
                .stream()
                .collect(Collectors.toMap(k -> toCamelCase(k.getKey()), Map.Entry::getValue));
    }
}
