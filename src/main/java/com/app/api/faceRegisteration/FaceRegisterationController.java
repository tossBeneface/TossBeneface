package com.app.api.faceRegisteration;

import com.app.api.file.service.FileUploadService;
import com.app.domain.member.entity.Member;
import com.app.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 얼굴 등록/조회/삭제 컨트롤러
 */
@RestController
@RequestMapping("/api/faces")
@RequiredArgsConstructor
public class FaceRegisterationController {

    private final FileUploadService fileUploadService;  // S3 업로드/삭제 로직 재사용
    private final FaceRegisterationRepository faceRegisterationRepository; // DB 접근
    private final MemberRepository memberRepository; // Member 테이블 접근

    /**
     * 1) S3에 파일 업로드 → URL 획득
     * 2) DB 저장 (member_id 포함)
     */
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFace(
            @RequestParam("memberId") Long memberId,
            @RequestParam("file") MultipartFile file
    ) {
        try {
            // 1) memberId를 통해 회원 조회
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new IllegalArgumentException("해당 memberId(" + memberId + ")를 찾을 수 없습니다."));

            // 2) S3에 업로드하고 URL 획득
            String imageUrl = fileUploadService.uploadFile(file);

            // 3) DB 저장 (✅ userName 제거)
            FaceRegisterationEntity entity = new FaceRegisterationEntity(member, imageUrl);
            FaceRegisterationEntity savedEntity = faceRegisterationRepository.save(entity);

            return ResponseEntity.ok(savedEntity);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("업로드 실패: " + e.getMessage());
        }
    }

    /**
     * 특정 회원(memberId)의 얼굴 데이터 조회
     */
    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<FaceRegisterationDTO>> getFacesByMember(@PathVariable Long memberId) {
        List<FaceRegisterationEntity> entities = faceRegisterationRepository.findByMember_MemberId(memberId);

        // 🔹 Entity 리스트 -> DTO 리스트 변환
        List<FaceRegisterationDTO> dtos = entities.stream()
                .map(FaceRegisterationDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    /**
     * DB에 있는 모든 얼굴 정보 조회
     */
    @GetMapping("/all")
    public ResponseEntity<List<FaceRegisterationDTO>> getAllFaces() {
        List<FaceRegisterationEntity> entities = faceRegisterationRepository.findAll();

        List<FaceRegisterationDTO> dtos = entities.stream()
                .map(FaceRegisterationDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    /**
     * 특정 ID로 얼굴 데이터 단건 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getFaceById(@PathVariable Long id) {
        try {
            FaceRegisterationEntity entity = faceRegisterationRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("해당 ID(" + id + ")를 찾을 수 없습니다."));

            return ResponseEntity.ok(entity);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
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
            FaceRegisterationEntity entity = faceRegisterationRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("해당 ID(" + id + ")를 찾을 수 없습니다."));

            // 2) S3에서 삭제
            fileUploadService.deleteFileFromS3(entity.getImageUrl());

            // 3) DB에서 삭제
            faceRegisterationRepository.delete(entity);

            return ResponseEntity.noContent().build(); // 204
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("삭제 실패: " + e.getMessage());
        }
    }
}