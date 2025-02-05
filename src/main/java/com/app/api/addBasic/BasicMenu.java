package com.app.api.addBasic;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "basic_menu")
public class BasicMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String cafe;

    private String img; // S3 또는 기본 이미지 URL

    @Column(nullable = false)
    private String menu;


    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private int stock;

    // 기본 생성자
    public BasicMenu() {
    }

    // 생성자
    public BasicMenu(String cafe, String menu, String img, double price, int stock) {
        this.cafe = cafe;
        this.img = img;
        this.menu = menu;
        this.price = price;
        this.stock = stock;
    }
}
