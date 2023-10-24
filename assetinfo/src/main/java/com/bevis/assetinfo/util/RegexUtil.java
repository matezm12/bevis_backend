package com.bevis.assetinfo.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.bevis.assetinfo.util.StringCastUtil.mapUnderscore;

public final class RegexUtil {

    private static final Pattern LINKS_PATTERN = Pattern.compile("\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");

    private static final Pattern ASSET_ID_PATTERN = Pattern.compile("^\\w{6}$");

    public static boolean validateLink(String s) {
        return LINKS_PATTERN.matcher(s).matches();
    }

    public static boolean isAssetId(String s) {
        return ASSET_ID_PATTERN
                .matcher(s)
                .matches();
    }

    public static boolean isAssetField(String fieldName) {
        return Pattern.compile("^(\\w*([Aa]ssetId|asset_id))$")
                .matcher(fieldName)
                .matches();
    }

    public static boolean isSkuField(String fieldName) {
        return Pattern.compile("^sku$", Pattern.CASE_INSENSITIVE)
                .matcher(fieldName)
                .matches();
    }

    public static String mapGroupName(String fullGroupName) {
        Pattern pattern = Pattern.compile("^(\\w*)(([Aa]ssetId)|(_asset_id))$");
        Matcher matcher = pattern.matcher(fullGroupName);
        if (matcher.matches()) {
            return StringUtils.capitalize(matcher.group(1));
        }
        return mapUnderscore(fullGroupName);
    }
}
