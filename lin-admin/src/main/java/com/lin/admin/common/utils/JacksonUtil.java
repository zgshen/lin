package com.lin.admin.common.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class JacksonUtil {

    private static ObjectMapper objectMapper;

    private JacksonUtil() {
        throw new UnsupportedOperationException();
    }

    static {
        objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    public static <T> T readValue(String jsonStr, Class<T> valueType) throws IOException {
        return objectMapper.readValue(jsonStr, valueType);
    }

    public static <T> T readValue(String jsonStr, TypeReference<T> valueTypeRef) throws IOException {
        return objectMapper.readValue(jsonStr, valueTypeRef);
    }

    public static <T> T readValue(String jsonStr, JavaType collectionType) throws IOException {
        return objectMapper.readValue(jsonStr, collectionType);
    }

    public static <T> String toJson(T src) throws IOException {
        return src instanceof String ? (String) src : objectMapper.writeValueAsString(src);
    }

    public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

}
