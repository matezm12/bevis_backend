package com.bevis.assetimport.codereadr;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Map;

public final class DeserializerUtil {
    public static <T> T deserialize(Map<String, String> data, Class<T> clazz){
        final Gson gson = new Gson();
        final String json = gson.toJson(data);
        try {
            return new ObjectMapper().readValue(json, clazz);
        } catch (IOException e) {
            throw new CodeReadrException("Error with CodeReadrRecord deserialization", e);
        }
    }
}
