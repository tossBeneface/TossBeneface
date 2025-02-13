package com.app.api.UserDataTest;

import com.app.api.CardBenefit.CardBenefitEntity;
import com.app.api.UserDataTest.dto.UserCardListDto;
import com.app.domain.card.entity.Card;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDataTestRepository extends JpaRepository<UserDataTestEntity, Long> {

    // ✅ 특정 회원(memberId)의 보유 카드 목록 조회 (corcompany + card 조합)
    @Query("SELECT DISTINCT u.cardName, u.cardCompany FROM UserDataTestEntity u WHERE u.member.memberId = :memberId")
    List<Object[]> findCardDetailsByMemberId(@Param("memberId") Long memberId);

    // ✅ 특정 회원(memberId)의 카드별 혜택 정보 조회 (corcompany + card를 기준으로 card_benefit 테이블 조인)
    @Query("""
        SELECT cb FROM CardBenefitEntity cb 
        JOIN UserDataTestEntity u ON cb.cardName = u.cardName AND cb.cardCompany = u.cardCompany
        WHERE u.member.memberId = :memberId
    """)
    List<CardBenefitEntity> findCardBenefitsByMemberId(@Param("memberId") Long memberId);

    // ✅ 특정 회원(memberId)의 재무 관련 데이터 조회 (card, corcompany, lastPer, payAmount, monthlySplit, nowPer, accrueBenefit)
    @Query("SELECT u.cardName, u.cardCompany, u.lastPer, u.payAmount, u.monthlySplit, u.nowPer, u.accrueBenefit " +
            "FROM UserDataTestEntity u WHERE u.member.memberId = :memberId")
    List<Object[]> findFinancialDataByMemberId(@Param("memberId") Long memberId);

    // card 테이블과 조인하여 user가 보유한 카드 목록 가져옴
    @Query("""
    SELECT new com.app.api.UserDataTest.dto.UserCardListDto(
        u.id, c.cardName, c.cardCompany, c.cardImage, 
        COALESCE(u.accrueBenefit, 0), u.cardNumber, u.expiryDate, u.cvc
    ) 
    FROM UserDataTestEntity u 
    JOIN u.card c 
    WHERE u.member.memberId = :memberId
""")
    List<UserCardListDto> findCardListByMemberId(@Param("memberId") Long memberId);

    // card_name과 card_company로 card 테이블에서 카드 조회
    @Query("SELECT c FROM Card c WHERE c.cardName = :cardName AND c.cardCompany = :cardCompany")
    Optional<Card> findCardByNameAndCompany(@Param("cardName") String cardName, @Param("cardCompany") String cardCompany);

    // ✅ 특정 카드 조회 (카드 ID와 회원 ID로 조회하여, 본인의 카드만 삭제 가능하도록 함)
    @Query("SELECT u FROM UserDataTestEntity u WHERE u.id = :cardId AND u.member.memberId = :memberId")
    Optional<UserDataTestEntity> findCardByIdAndMemberId(@Param("cardId") Long cardId, @Param("memberId") Long memberId);

    // ✅ 특정 회원(memberId)의 카드 삭제 (회원 본인의 카드만 삭제 가능하도록)
    @Transactional
    @Modifying
    @Query("DELETE FROM UserDataTestEntity u WHERE u.id = :cardId AND u.member.memberId = :memberId")
    void deleteCardByIdAndMemberId(@Param("cardId") Long cardId, @Param("memberId") Long memberId);
}