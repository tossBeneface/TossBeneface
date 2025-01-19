package com.app.global.util;

public class FileUtils {

    private FileUtils() {
        // 유틸리티 클래스는 인스턴스화 방지
    }

    /**
     * URL에서 파일 이름 추출
     *
     * @param url 파일 URL
     * @return 파일 이름
     */
    public static String extractFileNameFromUrl(String url) {
        if (url == null || !url.contains("/")) {
            throw new IllegalArgumentException("유효하지 않은 URL입니다.");
        }
        return url.substring(url.lastIndexOf("/") + 1);
    }
}
