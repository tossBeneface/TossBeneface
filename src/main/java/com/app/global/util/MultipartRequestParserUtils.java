package com.app.global.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;

public class MultipartRequestParserUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * JSON 데이터를 특정 DTO 클래스로 변환
     *
     * @param request     MultipartHttpServletRequest
     * @param parameter   JSON 데이터의 파라미터 이름
     * @param valueType   변환할 DTO 클래스
     * @param <T>         DTO 타입
     * @return 변환된 DTO 객체
     * @throws IllegalArgumentException 잘못된 JSON 형식일 경우 예외 발생
     */
    public static <T> T parseJson(MultipartHttpServletRequest request, String parameter, Class<T> valueType) {
        String json = request.getParameter(parameter);

        try {
            return objectMapper.readValue(json, valueType);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("잘못된 JSON 형식입니다.", e);
        }
    }

    /**
     * 파일 데이터를 추출
     *
     * @param request MultipartHttpServletRequest
     * @param key     파일의 파라미터 이름
     * @return MultipartFile 리스트
     */
    public static List<MultipartFile> parseFiles(MultipartHttpServletRequest request, String key) {
        return request.getFiles(key);
    }
}
