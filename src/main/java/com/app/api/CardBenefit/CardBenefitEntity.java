package com.app.api.CardBenefit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.app.domain.common.BaseEntity;
import com.app.api.UserDataTest.UserDataTestEntity;
import com.app.domain.card.entity.Card;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "CARD_BENEFIT")
public class CardBenefitEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Card 컬럼
    @Column(name = "card_name", nullable = true)
    private String cardName;

    // corcompany 컬럼
    @Column(name = "card_company", nullable = true)
    private String cardCompany;

    // summary 컬럼
    @Column(name = "summary", nullable = true)
    private String summary;

    // Shop 컬럼
    @Column(name = "Shop", nullable = true)
    private String shop;

    // Benefit 컬럼
    @Column(name = "Benefit", nullable = true)
    private int benefit;

    // Limit_once 컬럼
    @Column(name = "Limit_once", nullable = true)
    private int limitOnce;

    // Limit_month 컬럼
    @Column(name = "Limit_month", nullable = true)
    private int limitMonth;

    // 추가된 칼럼
    @Column(name = "min_pay", nullable = true)
    private int minPay; // 혜택을 받기 위한 최소 결제 금액

    @Column(name = "min_per", nullable = true)
    private int minPer; // 혜택을 받기 위한 최소 실적

    @Column(name = "monthly", nullable = true)
    private int monthly; // 혜택을 받을 수 있는 횟수

//    @OneToMany(mappedBy = "cardBenefit", cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonIgnore // ✅ JSON 직렬화 시 userDataList 제외
//    private List<UserDataTestEntity> userDataList;

    // ✅ Card와의 외래키 설정 추가
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", nullable = false) // card_id는 card 테이블의 id와 연결
    private Card card;
}