package com.app.api.UserDataTest;

import com.app.domain.card.entity.Card;
import com.app.api.CardBenefit.CardBenefitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

import java.util.List;

@Repository
public interface UserDataTestRepository extends JpaRepository<UserDataTestEntity, Long> {

    // ✅ 특정 회원(memberId)의 보유 카드 목록 조회 (corcompany + card 조합)
    @Query("SELECT DISTINCT u.cardName, u.cardCompany FROM UserDataTestEntity u WHERE u.member.memberId = :memberId")
    List<Object[]> findCardDetailsByMemberId(@Param("memberId") Long memberId);

    // ✅ 특정 회원(memberId)의 카드별 혜택 정보 조회 (corcompany + card를 기준으로 card_benefit 테이블 조인)
    @Query("""
        SELECT cb FROM CardBenefitEntity cb 
        JOIN UserDataTestEntity u ON cb.cardName = u.cardName AND cb.corp = u.cardCompany
        WHERE u.member.memberId = :memberId
    """)
    List<CardBenefitEntity> findCardBenefitsByMemberId(@Param("memberId") Long memberId);

    // ✅ 특정 회원(memberId)의 재무 관련 데이터 조회 (card, corcompany, lastPer, payAmount, monthlySplit, nowPer, accrueBenefit)
    @Query("SELECT u.cardName, u.cardCompany, u.lastPer, u.payAmount, u.monthlySplit, u.nowPer, u.accrueBenefit " +
            "FROM UserDataTestEntity u WHERE u.member.memberId = :memberId")
    List<Object[]> findFinancialDataByMemberId(@Param("memberId") Long memberId);

    // card 테이블과 조인하여 카드 정보 가져옴
    @Query("SELECT c.cardName, c.cardCompany, c.cardImage, IFNULL(u.accrueBenefit, 0) AS amount " +
            "FROM UserDataTestEntity u JOIN Card c ON c.id = u.card.id WHERE u.member.memberId = :memberId")
    List<UserDataTestEntity> findByMemberId(Long memberId);
}