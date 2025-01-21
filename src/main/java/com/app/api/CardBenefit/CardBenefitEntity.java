package com.app.api.CardBenefit;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "CARD_BENEFIT")
public class CardBenefitEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String cardName;

    @Column(nullable = false)
    private String corp;

    @Column(nullable = false)
    private String summary;

    @Column
    private int accumulation;

    @Column
    private int mileage;

    @Column
    private int discount;

    @Column
    private int limitOnce;

    @Column
    private int limitMonth;

    @Column
    private int minPay;

    @Column
    private int minPer;

    @Column
    private int monthly;
}