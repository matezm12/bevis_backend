package com.bevis.common.util;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.text.StringSubstitutor;

import java.util.Map;
import java.util.Optional;

public class StringUtil {
    public static String substringAfterLastIndexOf(String s1, String s2) {
        int index = s1.lastIndexOf(s2);
        if (index >= 0) {
            return s1.substring(s1.lastIndexOf(s2));
        }
        return "";
    }

    public static boolean isUrl(String s) {
        return s.startsWith("http://") || s.startsWith("https://");
    }

    public static String textToHex(String text){
        return Hex.encodeHexString(text.getBytes());
    }

    public static String toUpperCase(String s) {
        return Optional.ofNullable(s)
                .orElse("")
                .toUpperCase();
    }

    public static String toLowerCase(String s) {
        return Optional.ofNullable(s)
                .orElse("")
                .toLowerCase();
    }

    public static String formatString(String template, Map<String, String> params) {
        StringSubstitutor sub = new StringSubstitutor(params);
        return sub.replace(template);
    }
}
