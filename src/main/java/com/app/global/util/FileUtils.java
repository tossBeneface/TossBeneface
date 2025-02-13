package com.app.global.util;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

public class FileUtils {

    // 업로드 시 허용할 파일 확장자 목록 (필요에 따라 수정)
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "pdf");

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

    /**
     * 업로드된 MultipartFile의 확장자가 허용된 목록에 포함되어 있는지 검증
     *
     * @param file 업로드된 파일
     * @throws IllegalArgumentException 허용되지 않는 확장자일 경우 예외 발생
     */
    public static void validateFileExtension(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.contains(".")) {
            throw new IllegalArgumentException("파일 이름에 확장자가 없습니다.");
        }
        String extension = FilenameUtils.getExtension(originalFilename).toLowerCase();
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new IllegalArgumentException("허용되지 않는 파일 확장자입니다. 허용되는 확장자: " + ALLOWED_EXTENSIONS);
        }
    }

}
