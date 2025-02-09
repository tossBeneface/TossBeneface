package com.app.domain.order.entity;

import com.app.domain.card.entity.Card;
import com.app.domain.member.entity.Member;
import jakarta.persistence.*;

public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String orderMenu;
    private Integer price;


    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    @ManyToOne
    @JoinColumn(name = "card_id")
    private Card card;


}
