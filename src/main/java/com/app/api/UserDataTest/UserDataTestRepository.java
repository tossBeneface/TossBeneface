package com.app.api.UserDataTest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDataTestRepository extends JpaRepository<UserDataTestEntity, Long> {
    // JpaRepository를 상속받아 기본 CRUD 메서드를 사용합니다.
    List<UserDataTestEntity> findByUsername(String username);
}