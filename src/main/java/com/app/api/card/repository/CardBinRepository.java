package com.app.api.card.repository;

import com.app.domain.card.entity.CardBin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CardBinRepository extends JpaRepository<CardBin, Long> {
    @Query("SELECT cb FROM CardBin cb WHERE cb.cardBin = :cardNumber")
    Optional<CardBin> findByCardNumber(@Param("cardNumber") String cardNumber);
}
