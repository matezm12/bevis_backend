package com.bevis.assetimport.util;

import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public final class CodeReadrBodyConverterUtil {

    public static Map<String, String> getCodeReadrArgumentsMap(String data) {
        if (data == null){
            return null;
        }

        List<String> argumentsList = Arrays.asList(data.split("&"));
        Map<String, String> arguments = new HashMap<>();
        argumentsList.forEach(x-> {
            String[] keyValue = x.split("=");
            if (keyValue.length >= 2) {
                arguments.put(decode(keyValue[0]), decode(keyValue[1]));
            }
        });
        return arguments;
    }

    private static String decode(String text){
        try {
            return URLDecoder.decode(text, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("Error decode: {}", text);
            return null;
        }
    }
}
