package com.app.api.qnaboard.controller;

import com.app.api.qnaboard.dto.QnaBoardDto;
import com.app.api.qnaboard.service.QnaBoardInfoService;
import com.app.global.util.JsonUtils;
import com.app.global.util.MultipartRequestParserUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;

@Tag(name = "qnaboard", description = "qna게시판 게시글 API")
@RestController
@RequestMapping("/api/qnaboard")
@RequiredArgsConstructor
public class QnaBoardController {

    private final QnaBoardInfoService qnaBoardInfoService;

    @GetMapping("/test")
    public ResponseEntity<String> testInterceptor() {
        return ResponseEntity.ok("인터셉터가 동작합니다!");
    }

    @Tag(name = "qnaboard")
    @Operation(summary = "게시글 작성 API", description = "로그인 해서 받은 accessToken을 포함해 request, 파일 업로드 되게 하려면 컨트롤러 수정해야 - postman에서는 파일업로드 테스트 가능")
    @PostMapping("/create")
    public ResponseEntity<Long> createQnaBoard(@RequestPart("requestDto") String requestDtoJson, // JSON 문자열로 받음
                                               @RequestPart(value = "files", required = false) List<MultipartFile> files
                                               ) {
        try {
            System.out.println("Request DTO JSON: " + requestDtoJson);
            System.out.println("Files: " + (files != null ? files.size() : "No files"));

            // JSON 문자열을 QnaBoardDto.Request 객체로 변환 (JsonUtils 사용)
            JsonUtils jsonUtils = new JsonUtils();
            QnaBoardDto.Request requestDto = jsonUtils.deserialize(requestDtoJson, QnaBoardDto.Request.class);

            // 파일 데이터가 존재하면 requestDto에 설정
            if (files != null) {
                requestDto.setFiles(files);
            }

            // 게시글 생성 서비스 호출
            Long qnaBoardId = qnaBoardInfoService.createQnaBoard(requestDto);
            return ResponseEntity.ok(qnaBoardId);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Tag(name = "qnaboard")
    @Operation(summary = "게시글 상세 API", description = "accessToken을 포함해 request, 게시글 단건 조회")
    @GetMapping("/{qnaBoardId}")
    public ResponseEntity<QnaBoardDto.Response> getQnaBoard(@PathVariable("qnaBoardId") Long qnaBoardId) {
        QnaBoardDto.Response responseDto = qnaBoardInfoService.getQnaBoardById(qnaBoardId);
        return ResponseEntity.ok(responseDto);
    }

    @Tag(name = "qnaboard")
    @Operation(summary = "게시글 목록 API", description = "accessToken을 포함해 request, 게시글 전체 조회")
    @GetMapping
    public ResponseEntity<List<QnaBoardDto.Summary>> getAllQnaBoards() {
        List<QnaBoardDto.Summary> qnaBordSummaries = qnaBoardInfoService.getQnaBoardSummaries();
        return ResponseEntity.ok(qnaBordSummaries);
    }

    @Tag(name = "qnaboard")
    @Operation(summary = "게시글 수정 API", description = "accessToken을 포함해 request, 게시글 수정 - 주의 현재 첨부파일이 덮어쓰기가 안되고 누적됨 수정필요")
    @PutMapping("/{qnaBoardId}")
    public ResponseEntity<Long> updateQnaBoard(
            @PathVariable("qnaBoardId") Long qnaBoardId,
            MultipartHttpServletRequest request) {

        // JSON 데이터 파싱
        QnaBoardDto.UpdateRequest updateRequest = MultipartRequestParserUtils.parseJson(request, "requestDto", QnaBoardDto.UpdateRequest.class);

        // 파일 데이터 파싱
        List<MultipartFile> files = MultipartRequestParserUtils.parseFiles(request, "files");

        // 서비스 호출
        Long updatedQnaBoardId = qnaBoardInfoService.updateQnaBoard(qnaBoardId, updateRequest, files);

        return ResponseEntity.ok(updatedQnaBoardId);
    }

    @Tag(name = "qnaboard")
    @Operation(summary = "게시글 삭제 API - 미구현 아직 테스트 하지 X", description = "게시글을 실제로 삭제하지 않고 database에서 status값 변경 처리")
    @DeleteMapping("/{qnaBoardId}")
    public ResponseEntity<Void> deleteQnaBoard(@PathVariable("qnaBoardId") Long qnaBoardId) {
        qnaBoardInfoService.deleteQnaBoard(qnaBoardId);
        return ResponseEntity.ok().build();
    }
}
