package com.app.config;

import com.app.api.addBasic.BasicMenu;
import com.app.api.addBasic.BasicMenuRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class BasicMenuDataInitializer implements CommandLineRunner {

    private final BasicMenuRepository basicMenuRepository;

    public BasicMenuDataInitializer(BasicMenuRepository basicMenuRepository) {
        this.basicMenuRepository = basicMenuRepository;
    }

    @Override
    public void run(String... args) {
        insertMenuIfNotExists("카페A", "아메리카노", "https://example.com/americano.jpg", 4500, 50);
        insertMenuIfNotExists("카페A", "카페라떼", "https://example.com/cafelatte.jpg", 5000, 30);
        insertMenuIfNotExists("카페B", "바닐라라떼", "https://example.com/vanillalatte.jpg", 5500, 40);
        insertMenuIfNotExists("카페B", "카라멜 마키아토", "https://example.com/caramelmacchiato.jpg", 6000, 25);
        System.out.println("✅ 기본 메뉴 데이터 삽입 완료!");
    }

    private void insertMenuIfNotExists(String cafe, String menu, String img, double price, int stock) {
        Optional<BasicMenu> existingMenu = basicMenuRepository.findByCafeAndMenu(cafe, menu);
        if (existingMenu.isEmpty()) {
            basicMenuRepository.save(new BasicMenu(cafe, menu, img, price, stock));
            System.out.println("✅ 메뉴 추가됨: " + menu + " (" + cafe + ")");
        } else {
            System.out.println("⚠ 이미 존재하는 메뉴: " + menu + " (" + cafe + ")");
        }
    }
}

