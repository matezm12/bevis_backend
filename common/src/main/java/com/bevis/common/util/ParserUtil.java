package com.bevis.common.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

public final class ParserUtil {

    public static Map<String, Object> getMapFromResponse(String response) {
        Type type = getMapType();
        return new Gson().fromJson(response, type);
    }

    private static Type getMapType() {
        return new TypeToken<Map<String, Object>>() {
        }.getType();
    }
}
