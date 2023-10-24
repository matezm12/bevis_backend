package com.bevis.blockchain.gateway;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Optional;

public final class JsonParser {

    public static <T> Optional<T> parseJsonString(String json, Class<T> clazz) {
        try {
            return Optional.ofNullable(new ObjectMapper().readValue(json, clazz));
        } catch (IOException e) {
            return Optional.empty();
        }
    }
}
