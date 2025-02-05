package com.app.api.UserDataTest;

import com.app.domain.common.BaseEntity;
import com.app.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.app.api.CardBenefit.CardBenefitEntity;

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
    @JoinColumn(name = "member_id", nullable = false) // 외래키 컬럼 이름
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_benefit_id", nullable = false)
    private CardBenefitEntity cardBenefit;

    @Column(name = "Card", nullable = false)
    private String card;

    @Column(name = "corcompany", nullable = false)
    private String corcompany;

    @Column(name = "Last_per", nullable = false)
    private int lastPer;

    @Column(name = "date", nullable = false)
    private String date;

    @Column(name = "Pay_amount", nullable = false)
    private int payAmount;

    @Column(name = "Monthly_split", nullable = false)
    private int monthlySplit;

    @Column(name = "Now_per", nullable = false)
    private int nowPer;

    @Column(name = "Accrue_benefit", nullable = false)
    private int accrueBenefit;

}