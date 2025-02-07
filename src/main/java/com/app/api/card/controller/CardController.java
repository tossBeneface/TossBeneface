//package com.app.api.card.controller;
//
//import com.app.api.card.dto.CardDto;
//import com.app.api.card.service.CardService;
//import com.app.domain.card.entity.Card;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.util.List;
//
//@RestController
//public class CardController {
//
//    @Autowired
//    private CardService cardService;
//
//    @PostMapping("/register-card")
//    public String registerCard(@RequestBody CardDto cardDto) {
//        try {
//            Card card = cardService.registerCard(cardDto);
//            return "카드 등록 성공! 카드 ID: " + card.getId();
//        } catch (Exception e) {
//            return "카드 등록 실패: " + e.getMessage();
//        }
//    }
//
//    @GetMapping("/cards/{customerKey}")
//    public List<Card> getCards(@PathVariable String customerKey) {
//        try {
//            return cardService.getCardsByCustomerKey(customerKey);  // customerKey에 해당하는 카드 목록 조회
//        } catch (Exception e) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "카드 정보 조회 실패", e);
//        }
//    }
//}
