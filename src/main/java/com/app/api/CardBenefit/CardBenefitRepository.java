package com.app.api.CardBenefit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardBenefitRepository extends JpaRepository<CardBenefitEntity, Long> {

    // ✅ 특정 가게(shop)에서 사용 가능한 카드 혜택 조회
    List<CardBenefitEntity> findByShop(String shop);

    // ✅ corcompany와 cardName을 기준으로 카드 혜택 조회
    Optional<CardBenefitEntity> findByCardNameAndCorp(String cardName, String corp);
}