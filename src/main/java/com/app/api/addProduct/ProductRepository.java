package com.app.api.addProduct;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // cafe가 주어진 brand 문자열과 정확히 같은 모든 상품 조회
    // (대소문자 구분 필요에 따라 ignoreCase나 Contains 추가 가능)
    List<Product> findByCafe(String cafe);
}


