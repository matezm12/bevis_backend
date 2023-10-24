package com.bevis.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public final class DeserializerUtil {
    public static  <T> T deserializeJsonToObject(String jsonString, Class<T> clazz){
        return new Gson().fromJson(jsonString, clazz);
    }

    public static <T> T deserializeObjectToPojo(Object o, Class<T> clazz) {
        final ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(o, clazz);
    }

    public static  <T> T deserializeJsonFileToObject(String jsonFilePath, Class<T> clazz){
        try {
            String jsonString = new String(Files.readAllBytes(Paths.get(jsonFilePath)));
            return new Gson().fromJson(jsonString, clazz);
        } catch (IOException e){
            throw new RuntimeException("File not found " + jsonFilePath);
        }
    }
}
