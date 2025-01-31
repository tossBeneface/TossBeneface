package com.app.api.faceRegisteration;

import com.app.api.file.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Optional;

/**
 * 얼굴 등록/조회/삭제 등을 단일 Controller에서 처리하는 예시
 */
@RestController
@RequestMapping("/api/faces")
@RequiredArgsConstructor
public class FaceRegisterationController {

    private final FileUploadService fileUploadService;           // S3 업로드/삭제 로직 재사용
    private final FaceRegisterationRepository faceRegisterationRepository;  // DB 접근

    /**
     * 1) S3에 파일 업로드 → URL 획득
     * 2) DB 저장
     */
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFace(
            @RequestParam("userName") String userName,
            @RequestParam("file") MultipartFile file
    ) {
        try {
            // 1) S3에 업로드하고 URL 획득
            String imageUrl = fileUploadService.uploadFile(file);

            // 2) DB 저장
            FaceRegisterationEntity entity = new FaceRegisterationEntity(userName, imageUrl);
            FaceRegisterationEntity savedEntity = faceRegisterationRepository.save(entity);

            return ResponseEntity.ok(savedEntity);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("업로드 실패: " + e.getMessage());
        }
    }

    /**
     * DB에 있는 모든 얼굴 정보 조회
     */
    @GetMapping("/all")
    public ResponseEntity<List<FaceRegisterationEntity>> getAllFaces() {
        try {
            List<FaceRegisterationEntity> entities = faceRegisterationRepository.findAll();
            return ResponseEntity.ok(entities);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 특정 ID로 얼굴 데이터 단건 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getFaceById(@PathVariable Long id) {
        try {
            Optional<FaceRegisterationEntity> optional = faceRegisterationRepository.findById(id);
            if (optional.isPresent()) {
                return ResponseEntity.ok(optional.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("해당 ID(" + id + ")를 찾을 수 없습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("조회 실패: " + e.getMessage());
        }
    }

    /**
     * 특정 ID로 얼굴 데이터 삭제 (S3 & DB)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFace(@PathVariable Long id) {
        try {
            // 1) DB에서 엔티티 조회
            Optional<FaceRegisterationEntity> optional = faceRegisterationRepository.findById(id);
            if (optional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("해당 ID(" + id + ")를 찾을 수 없습니다.");
            }
            FaceRegisterationEntity entity = optional.get();

            // 2) S3에서 삭제
            fileUploadService.deleteFileFromS3(entity.getImageUrl());

            // 3) DB에서 삭제
            faceRegisterationRepository.delete(entity);

            return ResponseEntity.noContent().build(); // 204
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("삭제 실패: " + e.getMessage());
        }
    }
}