package com.app.api.UserDataTest;

import com.app.domain.common.BaseEntity;
import com.app.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.app.api.CardBenefit.CardBenefitEntity;
import com.app.domain.card.entity.Card;

@Getter
@Setter
@Entity
@Table(name = "user_data_test")
public class UserDataTestEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Member 엔티티와 외래키 관계 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    // Card 엔티티와 외래키 관계 설정 (추가)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

    // CardBenefit 엔티티와 외래키 관계 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_benefit_id", nullable = false)
    private CardBenefitEntity cardBenefit;

    // 카드 정보 관련 필드 (추가)
    @Column(name = "card_name", nullable = false)
    private String cardName;  // 변수명 변경 (일관성 유지)

    @Column(name = "card_company", nullable = false)
    private String cardCompany;  // 변수명 변경 (일관성 유지)

    @Column(name = "card_number", nullable = false, length = 20)
    private String cardNumber; // 카드번호 (int → String 변경)

    @Column(name = "expiry_date", nullable = false, length = 10)
    private String expiryDate; // 유효기간 (int → String 변경)

    @Column(name = "cvc", nullable = false)
    private int cvc; // CVC 번호

    @Column(name = "pwd", nullable = false)
    private int pwd; // 카드 비밀번호

    // 실적 및 혜택 정보
    @Column(name = "last_per", nullable = false)
    private int lastPer; // 전월 실적

    @Column(name = "now_per", nullable = false)
    private int nowPer; // 금월 실적

    @Column(name = "card_limit", nullable = false)
    private int cardLimit; // 카드 한도

    @Column(name = "monthly_split", nullable = false)
    private int monthlySplit; // 혜택 받은 횟수

    @Column(name = "accrue_benefit", nullable = false)
    private int accrueBenefit; // 혜택 받은 금액

    // 날짜 및 결제 관련 필드
    @Column(name = "date")
    private String date; // 오늘 날짜

    @Column(name = "pay_amount")
    private Integer payAmount; // 결제 금액 (nullable 가능)
}
