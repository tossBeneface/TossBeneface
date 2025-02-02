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

    private final FileUploadService fileUploadService;           // S3 업로드/삭제 로직 재사용
    private final FaceRegisterationRepository faceRegisterationRepository;  // DB 접근
    private final MemberRepository memberRepository;  // Member 테이블 접근

    /**
     * 얼굴 데이터 업로드
     * @param memberId 회원 ID
     * @param userName 사용자 이름
     * @param file 업로드 파일
     * @return 저장된 얼굴 데이터
     */
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFace(
            @RequestParam("memberId") Long memberId,
            @RequestParam("userName") String userName,
            @RequestParam("file") MultipartFile file
    ) {
        try {
            // 1) 회원 조회
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new IllegalArgumentException("해당 memberId(" + memberId + ")를 찾을 수 없습니다."));

            // 2) 파일 업로드
            String imageUrl = fileUploadService.uploadFile(file);

            // 3) FaceRegisterationEntity 저장
            FaceRegisterationEntity entity = new FaceRegisterationEntity(member, userName, imageUrl);
            FaceRegisterationEntity savedEntity = faceRegisterationRepository.save(entity);

            // 4) **DTO**로 변환해서 응답 (Lazy 로딩 문제 방지)
            return ResponseEntity.ok(new FaceRegisterationDTO(savedEntity));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
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

        List<FaceRegisterationDTO> dtos = entities.stream()
                .map(FaceRegisterationDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    /**
     * 사용자 이름(userName)으로 얼굴 데이터 조회
     */
    @GetMapping("/username/{userName}")
    public ResponseEntity<List<FaceRegisterationDTO>> getFacesByUserName(@PathVariable String userName) {
        List<FaceRegisterationEntity> entities = faceRegisterationRepository.findByUserName(userName);

        List<FaceRegisterationDTO> dtos = entities.stream()
                .map(FaceRegisterationDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    /**
     * 모든 얼굴 데이터 조회
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
     * 특정 ID로 얼굴 데이터 조회
     */
    @GetMapping("/id/{id}")
    public ResponseEntity<?> getFaceById(@PathVariable Long id) {
        try {
            FaceRegisterationEntity entity = faceRegisterationRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("해당 ID(" + id + ")를 찾을 수 없습니다."));
            return ResponseEntity.ok(entity);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("조회 실패: " + e.getMessage());
        }
    }

    /**
     * 특정 ID로 얼굴 데이터 삭제
     */
    @DeleteMapping("/id/{id}")
    public ResponseEntity<?> deleteFace(@PathVariable Long id) {
        try {
            // DB에서 엔티티 조회
            FaceRegisterationEntity entity = faceRegisterationRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("해당 ID(" + id + ")를 찾을 수 없습니다."));

            // S3에서 삭제
            fileUploadService.deleteFileFromS3(entity.getImageUrl());

            // DB에서 삭제
            faceRegisterationRepository.delete(entity);

            return ResponseEntity.noContent().build(); // 204
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("삭제 실패: " + e.getMessage());
        }
    }
}