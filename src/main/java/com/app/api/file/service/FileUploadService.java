package com.app.api.file.service;


import com.app.domain.qnaboard.entity.Attachment;
import com.app.domain.qnaboard.repository.AttachmentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class FileUploadService {

    private final S3Client s3Client;
    private final String bucketName = "ai0310bucket";
    private final AttachmentRepository attachmentRepository;

    public String uploadFile(MultipartFile file) {
        String fileName = generateUniqueFileName(file.getOriginalFilename());

        try {
            // S3에 파일 업로드
            s3Client.putObject(builder -> builder.bucket(bucketName).key(fileName),
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            // S3 URL 생성
            return String.format("https://%s.s3.ap-southeast-2.amazonaws.com/%s", bucketName, fileName);

        } catch (IOException e) {
            throw new RuntimeException("파일 업로드 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * 단일 파일 수정 메서드
     */
    public Attachment updateFile(Long fileId, MultipartFile file) {
        Attachment existingAttachment = attachmentRepository.findById(fileId)
                .orElseThrow(() -> new IllegalArgumentException("첨부파일을 찾을 수 없습니다."));

        // S3에 새 파일 업로드
        String newFileUrl = uploadFile(file);

        // 기존 Attachment 수정
        existingAttachment.updateFile(newFileUrl);
        return attachmentRepository.save(existingAttachment);
    }
    /**
     * 여러 파일 수정 메서드
     */
    public List<Attachment> updateMultipleFiles(List<Long> fileIds, List<MultipartFile> files) {
        if (fileIds.size() != files.size()) {
            throw new IllegalArgumentException("파일 ID와 업로드된 파일 개수가 일치하지 않습니다.");
        }

        List<Attachment> updatedAttachments = new ArrayList<>();

        for (int i = 0; i < files.size(); i++) {
            Long fileId = fileIds.get(i);
            MultipartFile file = files.get(i);

            // 단일 파일 수정 처리
            Attachment updatedFile = updateFile(fileId, file);
            updatedAttachments.add(updatedFile);
        }

        return updatedAttachments;
    }

    private String generateUniqueFileName(String originalFilename) {
        return UUID.randomUUID() + "_" + originalFilename;
    }
}
