package com.app.api.addBasic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

import java.util.List;

@Repository
public interface BasicMenuRepository extends JpaRepository<BasicMenu, Long> {
    Optional<BasicMenu> findByCafeAndMenu(String cafe, String menu);
    List<BasicMenu> findByCafe(String cafe);
}



