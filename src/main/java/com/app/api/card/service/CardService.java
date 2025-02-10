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
package com.app.api.card.service;

import com.app.api.card.repository.CardRepository;
import com.app.domain.card.entity.Card;
import org.springframework.stereotype.Service;

@Service
public class CardService {

    private final CardRepository cardRepository;

    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public String getCardImage(String cardName, String cardCompany) {
        // CardRepository에서 cardName과 cardCompany로 Card 엔티티를 조회하고,
        // 해당 엔티티의 cardImage를 반환합니다.
        return cardRepository.findByCardNameAndCardCompany(cardName, cardCompany)
                .map(Card::getCardImage)
                .orElseThrow(() -> new RuntimeException("Card not found"));
    }
}