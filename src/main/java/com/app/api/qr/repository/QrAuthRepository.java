package com.app.api.qr.repository;

import com.app.domain.qr.entity.QrAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QrAuthRepository extends JpaRepository<QrAuth, Long> {
    Optional<QrAuth> findByMemberMemberIdAndNonce(Long memberId, String nonce);
}