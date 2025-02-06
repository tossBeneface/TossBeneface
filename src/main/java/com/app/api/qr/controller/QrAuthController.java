package com.app.api.qr.controller;

import com.app.api.qr.repository.QrAuthRepository;
import com.app.domain.qr.entity.QrAuth;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

        Optional<QrAuth> qrAuth = qrAuthRepository.findByMemberMemberIdAndNonce(memberId, nonce);
        if (qrAuth.isPresent()) {
            // 인증 성공 -> authenticated 값을 true로 변경
            QrAuth auth = qrAuth.get();
            auth.setAuthenticated(true);
            qrAuthRepository.save(auth);

            log.info("QR 인증 성공: {}", memberId);
            return ResponseEntity.ok("인증 성공! 다음 페이지로 이동 가능합니다.");
        } else {
            log.warn("QR 인증 실패: {}", memberId);
            return ResponseEntity.status(401).body("인증 실패! 올바르지 않은 QR 코드입니다.");
        }
    }
}

