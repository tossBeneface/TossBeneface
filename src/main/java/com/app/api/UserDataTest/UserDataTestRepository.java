package com.app.api.UserDataTest;

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
    @Query("SELECT DISTINCT u.card, u.corcompany FROM UserDataTestEntity u WHERE u.member.memberId = :memberId")
    List<Object[]> findCardDetailsByMemberId(@Param("memberId") Long memberId);

    // ✅ 특정 회원(memberId)의 카드별 혜택 정보 조회 (corcompany + card를 기준으로 card_benefit 테이블 조인)
    @Query("""
        SELECT cb FROM CardBenefitEntity cb 
        JOIN UserDataTestEntity u ON cb.cardName = u.card AND cb.corp = u.corcompany
        WHERE u.member.memberId = :memberId
    """)
    List<CardBenefitEntity> findCardBenefitsByMemberId(@Param("memberId") Long memberId);
}