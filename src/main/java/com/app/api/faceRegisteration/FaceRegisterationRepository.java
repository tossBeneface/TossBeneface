package com.app.api.faceRegisteration;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FaceRegisterationRepository extends JpaRepository<FaceRegisterationEntity, Long> {
    // 필요시 사용자 이름으로 검색하는 쿼리 메서드 등을 추가할 수 있음
    // e.g. List<FaceRegisterationEntity> findByUserName(String userName);
}
