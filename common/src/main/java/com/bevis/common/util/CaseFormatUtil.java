package com.bevis.common.util;

import com.google.common.base.CaseFormat;
import com.google.common.base.Strings;

import java.util.*;
import java.util.stream.Collectors;

public final class CaseFormatUtil {

    public static String toUnderscore(String s) {
        if (Strings.isNullOrEmpty(s)) {
            return s;
        }
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, s);
    }

    public static String toCamelCase(String s) {
        if (Strings.isNullOrEmpty(s) || !s.contains("_")) {
            return s;
        }
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, s);
    }

    public static Set<String> toUnderscore(Set<String> set) {
        return set.stream()
                .map(CaseFormatUtil::toUnderscore)
                .collect(Collectors.toSet());
    }

    public static <T> Map<String, T> makeCamelCaseFields(Map<String, T> original) {
        return original.entrySet()
                .stream()
                .collect(HashMap::new, (m, x) -> m.put(toCamelCase(x.getKey()), x.getValue()), HashMap::putAll);
    }

    public static <T> Map<String, T> makeUnderscoreFields(Map<String, T> original) {
        return original.entrySet()
                .stream()
                .collect(HashMap::new, (m, x) -> m.put(toUnderscore(x.getKey()), x.getValue()), HashMap::putAll);
    }

}
