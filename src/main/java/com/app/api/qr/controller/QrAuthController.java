package com.app.api.qr.controller;

import com.app.api.qr.repository.QrAuthRepository;
import com.app.domain.qr.entity.QrAuth;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
public class QrAuthController {

    private final QrAuthRepository qrAuthRepository;

    @GetMapping("/qr/authenticate")
    public ResponseEntity<?> authenticateQR(
            @RequestParam Long memberId,
            @RequestParam String nonce) {

        Optional<QrAuth> qrAuthOptional = qrAuthRepository.findByMemberMemberIdAndNonce(memberId, nonce);

        if (qrAuthOptional.isEmpty()) {
            log.warn("❌ QR 인증 실패 (잘못된 nonce): memberId={}", memberId);
            return ResponseEntity.status(401).body("인증 실패! 올바르지 않은 QR 코드입니다.");
        }

        QrAuth qrAuth = qrAuthOptional.get();

        // ✅ 이미 인증된 QR 코드인지 확인
        if (qrAuth.isAuthenticated()) {
            log.warn("🚫 QR 인증 실패 (이미 인증됨): memberId={}", memberId);
            return ResponseEntity.status(403).body("인증 실패! 이미 사용된 QR 코드입니다.");
        }

        // ✅ 5분(300초) 초과 여부 확인
        LocalDateTime createdAt = qrAuth.getCreatedAt();
        if (Duration.between(createdAt, LocalDateTime.now()).getSeconds() > 300) {
            log.warn("⏳ QR 인증 실패 (시간 초과): memberId={}", memberId);
            return ResponseEntity.status(401).body("인증 실패! QR 코드가 만료되었습니다.");
        }

        // ✅ 인증 성공 -> authenticated 값을 true로 변경
        qrAuth.setAuthenticated(true);
        qrAuthRepository.save(qrAuth);

        log.info("✅ QR 인증 성공: memberId={}", memberId);
        return ResponseEntity.ok("인증 성공! 다음 페이지로 이동 가능합니다.");
    }
}
