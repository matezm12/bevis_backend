package com.bevis.common.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public final class JsonUtil {

    public static Map<String, Object> mapAttributes(Object attributes) {
        if (Objects.nonNull(attributes) && attributes instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> attrs = (Map<String, Object>) attributes;
            return new LinkedHashMap<>(attrs);
        }
        return new LinkedHashMap<>();
    }

}
