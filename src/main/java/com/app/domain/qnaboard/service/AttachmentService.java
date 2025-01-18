package com.app.domain.qnaboard.service;

import com.app.api.file.service.FileUploadService;
import com.app.domain.qnaboard.constant.FileStatus;
import com.app.domain.qnaboard.entity.Attachment;
import com.app.domain.qnaboard.entity.QnaBoard;
import com.app.domain.qnaboard.repository.AttachmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AttachmentService {

    private final FileUploadService fileUploadService;
    private final AttachmentRepository attachmentRepository;
    public List<Attachment> saveAttachments(List<MultipartFile> files, QnaBoard qnaBoard) {
        if (files == null || files.isEmpty()) {
            return Collections.emptyList();
        }

        return files.stream()
                .map(file -> {
                    Attachment attachment = createAttachment(file, qnaBoard);
                    qnaBoard.addAttachment(attachment);
                    return attachment;
                })
                .collect(Collectors.toList());
    }

    private Attachment createAttachment(MultipartFile file, QnaBoard qnaBoard) {
        // S3에 파일 업로드 후 URL 반환
        String fileUrl = fileUploadService.uploadFile(file);

        // Attachment 엔티티 생성 및 저장
        Attachment attachment = Attachment.builder()
                .qnaBoard(qnaBoard)
                .url(fileUrl)
                .filePath(file.getOriginalFilename())
                .fileType(file.getContentType())
                .fileStatus(FileStatus.ACTIVATE)
                .build();

        return attachmentRepository.save(attachment);
    }
}
