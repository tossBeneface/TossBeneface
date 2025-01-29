package com.app.global.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {
    private static final ObjectMapper mapper = new ObjectMapper();

    public JsonUtils() {
        super();
        // 추가적인 ObjectMapper 설정이 필요하면 여기에 작성
    }

    public <T> T deserialize(final String json, final Class<T> clazz) {
        if (json == null || clazz == null) {
            return null;
        }

        try {
            return mapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            // 예외를 처리하거나 로그를 출력
            System.err.println("JSON deserialization error: " + e.getMessage());
            return null; // 또는 예외를 다시 던질 수도 있음
        }
    }
}
