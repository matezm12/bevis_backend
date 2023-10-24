package com.bevis.assetinfo.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;

public final class StringCastUtil {
    public static String mapUnderscore(String s) {
        if (Strings.isBlank(s)) {
            return s;
        }
        return StringUtils.capitalize(s.replace('_', ' '));
    }
}
