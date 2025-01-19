package com.app.api.qnaboard.controller;

import com.app.api.qnaboard.dto.QnaBoardDto;
import com.app.api.qnaboard.service.QnaBoardInfoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

@RestController
@RequestMapping("/api/qnaboard")
@RequiredArgsConstructor
public class QnaBoardController {

    private final QnaBoardInfoService qnaBoardInfoService;

    @GetMapping("/test")
    public ResponseEntity<String> testInterceptor() {
        return ResponseEntity.ok("인터셉터가 동작합니다!");
    }

    @PostMapping("/create")
    public ResponseEntity<Long> createQnaBoard(MultipartHttpServletRequest request) {
        // JSON 데이터 추출
        String requestDtoJson = request.getParameter("requestDto");
        ObjectMapper objectMapper = new ObjectMapper();
        QnaBoardDto.Request requestDto;

        try {
            requestDto = objectMapper.readValue(requestDtoJson, QnaBoardDto.Request.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("잘못된 JSON 형식입니다.", e);
        }

        // 파일 데이터 추출
        List<MultipartFile> files = request.getFiles("files");
        requestDto.setFiles(files);

        // 서비스 호출
        Long qnaBoardId = qnaBoardInfoService.createQnaBoard(requestDto);
        return ResponseEntity.ok(qnaBoardId);
    }

    @GetMapping("/{qnaBoardId}")
    public ResponseEntity<QnaBoardDto.Response> getQnaBoard(@PathVariable("qnaBoardId") Long qnaBoardId) {
        QnaBoardDto.Response responseDto = qnaBoardInfoService.getQnaBoardById(qnaBoardId);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<QnaBoardDto.Summary>> getAllQnaBoards() {
        List<QnaBoardDto.Summary> qnaBordSummaries = qnaBoardInfoService.getQnaBoardSummaries();
        return ResponseEntity.ok(qnaBordSummaries);
    }

    @PutMapping("/{qnaBoardId}")
    public ResponseEntity<QnaBoardDto.Response> updateQnaBoard(
            @PathVariable("qnaBoardId") Long qnaBoardId,
            @RequestBody QnaBoardDto.UpdateRequest updateRequest) {

        QnaBoardDto.Response response = qnaBoardInfoService.updateQnaBoard(qnaBoardId, updateRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{qnaBoardId}")
    public ResponseEntity<Void> deleteQnaBoard(@PathVariable("qnaBoardId") Long qnaBoardId) {
        qnaBoardInfoService.deleteQnaBoard(qnaBoardId);
        return ResponseEntity.ok().build();
    }
}
