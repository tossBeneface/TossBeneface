//package com.app.api.card.service;
//
//import com.app.api.card.dto.CardDto;
//import com.app.api.card.repository.CardRepository;
//import com.app.domain.card.entity.Card;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class CardService {
//
//    @Autowired
//    private CardRepository cardRepository;
//
//    public Card registerCard(CardDto cardDto) {
//        // 카드 등록 로직
//        Card card = new Card();
//        card.setCardNumber(cardDto.getCardNumber());
//        card.setCardType(cardDto.getCardType());
//        card.setExpiryDate(cardDto.getExpiryDate());
//        card.setCustomerKey(cardDto.getCustomerKey());
//        return cardRepository.save(card);
//    }
//
//    public List<Card> getCardsByCustomerKey(String customerKey) {
//        return cardRepository.findByCustomerKey(customerKey);  // customerKey로 카드 정보 조회
//    }
//}
