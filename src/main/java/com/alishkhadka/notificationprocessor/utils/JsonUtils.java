package com.alishkhadka.notificationprocessor.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    protected JsonUtils() {
    }

    public static String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert object to json string", e);
        }
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return (T) objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse json object", e);
        }
    }
}
