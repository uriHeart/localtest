package com.argo.common.domain.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;

public final class JsonUtil {
    private static ObjectMapper mapper;

    private static ObjectMapper getMapper() {
        if (mapper == null) {
            mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        }
        return mapper;
    }

    public static <T>T read(final String json, final Class<T> clazz) {
        try {
            return getMapper().readValue(json, clazz);
        } catch (IOException e) {
            throw new RuntimeException("Json read error : ", e);
        }
    }

    public static String write(Object value) {
        try {
            return getMapper().writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Json write error : ", e);
        }
    }
}
