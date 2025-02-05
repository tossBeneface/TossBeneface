package com.app.api.addProduct;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.app.domain.common.BaseEntity;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto Increment
    private Long id;
//    @Column(nullable = false)
    private String cafe;
    private String menu;
    private String img;
    private int price;
    private int stock;
}

