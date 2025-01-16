package com.app.api.file.service;


import com.app.domain.qnaboard.constant.FileStatus;
import com.app.domain.qnaboard.entity.Attachment;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class FileUploadService {

    private final S3Client s3Client;
    private final String bucketName = "ai0310bucket";
    private final String region = "ap-southeast-2";


    public List<Attachment> uploadFiles(List<MultipartFile> files) {
        return files.stream()
                .map(file -> {
                    String fileName = generateUniqueFileName(file.getOriginalFilename());
                    try {
                        // 파일을 S3에 업로드
                        s3Client.putObject(builder -> builder.bucket(bucketName).key(fileName),
                                RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
                        // S3 URL 생성
                        String fileUrl = String.format("https://%s.s3.ap-southeast-2.amazonaws.com/%s", bucketName, fileName);


                        // Attachment 엔티티 생성 및 반환
                        return Attachment.builder()
                                .url(fileUrl)
                                .filePath(fileName)
                                .fileType(file.getContentType()) // 파일 타입 설정
                                .fileStatus(FileStatus.ACTIVATE) // 기본 상태값 설정
                                .build();

                    } catch (IOException e) {
                        throw new RuntimeException("파일 업로드 중 오류가 발생했습니다.", e);
                    }
                })
                .collect(Collectors.toList());
    }

    private String uploadFile(MultipartFile file) {
        String fileName = generateUniqueFileName(file.getOriginalFilename());

        try {
            // S3에 파일 업로드
            s3Client.putObject(builder -> builder.bucket(bucketName).key(fileName),
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            // 업로드한 파일의 URL 반환
            return getFileUrl(fileName);

        } catch (IOException e) {
            throw new RuntimeException("파일 업로드 중 오류가 발생했습니다.", e);
        }
    }

    private String getFileUrl(String fileName) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, fileName);
    }

    private String generateUniqueFileName(String originalFileName) {
        return System.currentTimeMillis() + "_" + originalFileName;
    }
}
