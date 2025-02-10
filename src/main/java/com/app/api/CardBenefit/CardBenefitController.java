package com.app.api.CardBenefit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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

    // 신규 API: carcompany와 card_name 파라미터로 Benefit, limit_once, limit_month, min_pay, min_per, monthly 반환
    @GetMapping("/get_details")
    public ResponseEntity<?> getCardBenefitDetails(
            @RequestParam("carcompany") String carcompany,
            @RequestParam("card_name") String cardName) {

        // fetch join 메서드를 사용하여 연관된 Card 엔티티까지 함께 로딩합니다.
        List<CardBenefitEntity> benefitList = repository.findByCardNameAndCardCompanyFetchCard(cardName, carcompany);

        if (benefitList != null && !benefitList.isEmpty()) {
            List<Map<String, Object>> response = new ArrayList<>();

            for (CardBenefitEntity entity : benefitList) {
                Map<String, Object> detail = new HashMap<>();
                detail.put("Benefit", entity.getBenefit());
                detail.put("limit_once", entity.getLimitOnce());
                detail.put("limit_month", entity.getLimitMonth());
                detail.put("min_pay", entity.getMinPay());
                detail.put("min_per", entity.getMinPer());
                detail.put("monthly", entity.getMonthly());

                // 연관된 Card 엔티티에서 cardImage 값을 가져옵니다.
                detail.put("card_image", entity.getCard().getCardImage());
                response.add(detail);
            }
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No card benefit found for given parameters");
        }
    }
}