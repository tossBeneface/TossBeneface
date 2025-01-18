package com.app.api.file.controller;

import com.app.api.file.service.FileUploadService;
import com.app.domain.qnaboard.entity.Attachment;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileUploadController {

    private final FileUploadService fileUploadService;

    /**
     * 단일 파일 수정 API
     */
    @PostMapping("/update/{fileId}")
    public ResponseEntity<Attachment> updateFile(
            @PathVariable Long fileId,
            @RequestPart("file") MultipartFile file) {
        Attachment updatedFile = fileUploadService.updateFile(fileId, file);
        return ResponseEntity.ok(updatedFile);
    }

    /**
     * 여러 파일 수정 API
     */
    @PostMapping("/update-multiple")
    public ResponseEntity<List<Attachment>> updateMultipleFiles(
            @RequestPart("files") List<MultipartFile> files,
            @RequestParam List<Long> fileIds) {
        List<Attachment> updatedFiles = fileUploadService.updateMultipleFiles(fileIds, files);
        return ResponseEntity.ok(updatedFiles);
    }
}
