package com.app.api.CardBenefit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CardBenefitRepository extends JpaRepository<CardBenefitEntity, Long> {

    // 기존 코드: 특정 가게(shop)에서 사용 가능한 카드 혜택 조회
    List<CardBenefitEntity> findByShop(String shop);

    // 기존 코드: corp(company)와 cardName을 기준으로 카드 혜택 조회 (여러 결과 반환)
    List<CardBenefitEntity> findByCardNameAndCardCompany(String cardName, String corp);

    // 추가: JPQL fetch join을 사용해 연관된 Card 엔티티(예: image 포함)도 함께 로딩
    @Query("SELECT cb FROM CardBenefitEntity cb JOIN FETCH cb.card c " +
            "WHERE cb.cardName = :cardName AND cb.cardCompany = :corp")
    List<CardBenefitEntity> findByCardNameAndCardCompanyFetchCard(@Param("cardName") String cardName,
                                                                  @Param("corp") String corp);
}