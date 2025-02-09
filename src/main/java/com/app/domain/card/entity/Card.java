package com.app.domain.card.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "card")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "card_name", nullable = false)
    private String cardName;

    @Column(name = "card_company", nullable = false)
    private String cardCompany;

    @Column(name = "card_image")
    private String cardImage;
}