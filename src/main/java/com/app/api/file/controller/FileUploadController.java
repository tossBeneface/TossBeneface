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

    @PostMapping("/upload")
    public ResponseEntity<List<Attachment>> uploadFiles(@RequestPart("files") List<MultipartFile> files) {
        List<Attachment> fileUrls = fileUploadService.uploadFiles(files);
        return ResponseEntity.ok(fileUrls);
    }
}
