package com.app.domain.card.entity;

import com.app.domain.member.entity.Member;
import com.app.global.resolver.memberInfo.MemberInfoDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cardNumber;
    private String cardType;
    private String expiryDate;
    private String customerKey;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
