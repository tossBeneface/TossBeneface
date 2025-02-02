package com.app.api.faceRegisteration;

import com.app.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface FaceRegisterationRepository extends JpaRepository<FaceRegisterationEntity, Long> {

    // 사용자 이름으로 검색
    List<FaceRegisterationEntity> findByUserName(String userName);

    // 이미지 URL로 검색
    Optional<FaceRegisterationEntity> findByImageUrl(String imageUrl);

    // ✅ 특정 회원(memberId)의 얼굴 데이터 조회 추가
    List<FaceRegisterationEntity> findByMember_MemberId(Long memberId);
}