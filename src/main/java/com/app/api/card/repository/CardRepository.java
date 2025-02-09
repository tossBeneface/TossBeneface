package com.app.api.card.repository;

import com.app.domain.card.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {
    Optional<Card> findByCardNameAndCardCompany(String cardName, String cardCompany);
}
