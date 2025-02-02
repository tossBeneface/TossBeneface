package com.app.api.CardBenefit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/card-benefits") // 공통 URL 경로
public class CardBenefitController {

    @Autowired
    private CardBenefitRepository repository;

    // POST 요청으로 데이터를 저장
    @PostMapping
    public String saveCardBenefit(@RequestBody CardBenefitEntity cardBenefit) {
        repository.save(cardBenefit);
        return "카드 혜택 데이터가 성공적으로 저장되었습니다!";
    }

    @GetMapping("/get_shop")
    public List<CardBenefitEntity> getCardBenefitsByShop(@RequestParam String shop) {
        return repository.findByShop(shop);
    }
}