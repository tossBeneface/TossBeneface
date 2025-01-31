package com.app.api.faceRegisteration;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface FaceRegisterationRepository extends JpaRepository<FaceRegisterationEntity, Long> {

    // 사용자 이름으로 검색하는 메서드 예시 (선택사항)
    List<FaceRegisterationEntity> findByUserName(String userName);

    // 이미지 URL로 검색하는 메서드 예시 (선택사항)
    Optional<FaceRegisterationEntity> findByImageUrl(String imageUrl);

}