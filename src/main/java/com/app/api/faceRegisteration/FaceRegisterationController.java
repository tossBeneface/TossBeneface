package com.app.api.faceRegisteration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("/api/faces")
public class FaceRegisterationController {

    @Autowired
    private FaceRegisterationRepository faceRegisterationRepository;

    // 얼굴 이미지 업로드 (등록)
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFace(
            @RequestParam("userName") String userName,
            @RequestParam("file") MultipartFile file
    ) {
        try {
            // MultipartFile → byte[]
            byte[] imageBytes = file.getBytes();

            // DB에 저장
            FaceRegisterationEntity entity = new FaceRegisterationEntity(userName, imageBytes);
            faceRegisterationRepository.save(entity);

            return new ResponseEntity<>("얼굴 이미지를 성공적으로 업로드했습니다.", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("업로드 실패: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // DB에 있는 모든 얼굴 정보 조회 (파이썬에서 쓰기 위해)
    @GetMapping("/all")
    public ResponseEntity<?> getAllFaces() {
        try {
            List<FaceRegisterationEntity> entities = faceRegisterationRepository.findAll();
            List<Map<String, Object>> response = new ArrayList<>();

            for (FaceRegisterationEntity entity : entities) {
                Map<String, Object> faceData = new HashMap<>();
                faceData.put("id", entity.getId());
                faceData.put("userName", entity.getUserName());
                faceData.put("imageData", Base64.getEncoder().encodeToString(entity.getImageData())); // 이미지 데이터를 Base64로 인코딩
                response.add(faceData);
            }

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("조회 실패: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 특정 ID(혹은 사용자이름)로 얼굴 데이터 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<?> getFaceById(@PathVariable Long id) {
        return faceRegisterationRepository.findById(id)
                // 여기서 제네릭을 명시적으로 <ResponseEntity<?>> 로 지정
                .<ResponseEntity<?>>map(face -> new ResponseEntity<>(face, HttpStatus.OK))
                .orElse(new ResponseEntity<>("해당 ID 없음", HttpStatus.NOT_FOUND));
    }
}