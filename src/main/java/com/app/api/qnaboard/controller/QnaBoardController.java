package com.app.api.qnaboard.controller;

import com.app.api.qnaboard.dto.QnaBoardDto;
import com.app.api.qnaboard.service.QnaBoardInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/qnaboard")
@RequiredArgsConstructor
public class QnaBoardController {

    private final QnaBoardInfoService qnaBoardService;

    @GetMapping("/test")
    public ResponseEntity<String> testInterceptor() {
        return ResponseEntity.ok("인터셉터가 동작합니다!");
    }

    @PostMapping("/create")
    public ResponseEntity<Long> createQnaBoard(
            @RequestPart(value = "requestDto") @Validated QnaBoardDto.Request requestDto,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        requestDto.setFiles(files);
        Long qnaBoardId = qnaBoardService.createQnaBoard(requestDto);
        return ResponseEntity.ok(qnaBoardId);
    }

    @GetMapping("/{qnaBoardId}")
    public ResponseEntity<QnaBoardDto.Response> getQnaBoard(@PathVariable Long qnaBoardId) {
        QnaBoardDto.Response responseDto = qnaBoardService.getQnaBoardById(qnaBoardId);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<QnaBoardDto.Response>> getAllQnaBoards() {
        List<QnaBoardDto.Response> responseDtos = qnaBoardService.getAllQnaBoards();
        return ResponseEntity.ok(responseDtos);
    }

    @PutMapping("/{qnaBoardId}")
    public ResponseEntity<Void> updateQnaBoard(
            @PathVariable Long qnaBoardId,
            @Validated @RequestBody QnaBoardDto.Request requestDto) {
        qnaBoardService.updateQnaBoard(qnaBoardId, requestDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{qnaBoardId}")
    public ResponseEntity<Void> deleteQnaBoard(@PathVariable Long qnaBoardId) {
        qnaBoardService.deleteQnaBoard(qnaBoardId);
        return ResponseEntity.ok().build();
    }
}
