package com.app.api.UserDataTest;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user_data_test")
public class UserDataTestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String cardName;

    @Column(nullable = false)
    private String corp;

    @Column(nullable = false)
    private int lastMonthPerformance;

    @Column(nullable = false)
    private String todayDate;

    @Column(nullable = false)
    private int paymentAmount;

    @Column(nullable = false)
    private int benefitCount;

    @Column(nullable = false)
    private int currentMonthPerformance;

    @Column(nullable = false)
    private int expectedBenefitScore;

    @Column(nullable = false, name = "username") // 칼럼명을 "username"으로 변경
    private String username;
}