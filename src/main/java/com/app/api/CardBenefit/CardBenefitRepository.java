package com.app.api.CardBenefit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardBenefitRepository extends JpaRepository<CardBenefitEntity, Long> {
    // JpaRepository를 확장하여 기본 CRUD 메서드 제공
    List<CardBenefitEntity> findByShop(String shop);
}