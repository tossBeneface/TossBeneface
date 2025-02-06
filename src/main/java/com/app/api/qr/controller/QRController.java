package com.app.api.qr.controller;

import com.app.api.qr.repository.QrAuthRepository;
import com.app.domain.member.entity.Member;
import com.app.domain.qr.entity.QrAuth;
import com.app.domain.member.repository.MemberRepository;
import com.app.global.resolver.memberInfo.MemberInfo;
import com.app.global.resolver.memberInfo.MemberInfoDto;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;

@RestController
@RequiredArgsConstructor
@Slf4j
public class QRController {

    private final SecureRandom secureRandom = new SecureRandom();
    private final QrAuthRepository qrAuthRepository;
    private final MemberRepository memberRepository; // ✅ Member 조회용

    @GetMapping("/qr/generate")
    public ResponseEntity<byte[]> generateQR(@MemberInfo MemberInfoDto memberInfoDto) throws WriterException, IOException {
        int width = 400;
        int height = 400;

        log.info("Received memberId: {}", memberInfoDto.getMemberId());

        // ✅ 1. 난수 (nonce) 생성
        String nonce = generateNonce();

        // ✅ 2. DB에 QrAuth 데이터 저장
        Member member = memberRepository.findById(memberInfoDto.getMemberId())
                .orElseThrow(() -> new RuntimeException("Member not found"));

        QrAuth qrAuth = new QrAuth();
        qrAuth.setMember(member);
        qrAuth.setNonce(nonce);
        qrAuth.setAuthenticated(false); // 인증 전 상태

        qrAuthRepository.save(qrAuth); // ✅ 저장

        // ✅ 3. QR 코드에 포함될 데이터 (memberId + nonce)
        String text = member.getMemberId() + "|" + nonce;
        log.info("Generated QR Text: {}", text);

        // ✅ 4. QR 코드 생성
        BitMatrix encode = new MultiFormatWriter()
                .encode(text, BarcodeFormat.QR_CODE, width, height);

        // ✅ 5. Output Stream 변환 후 응답 반환
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            MatrixToImageWriter.writeToStream(encode, "PNG", out);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(out.toByteArray());
        } catch (Exception e) {
            log.warn("QR Code 생성 중 오류 발생: {}", e.getMessage());
        }

        return ResponseEntity.status(500).build();
    }

    // ✅ 6. 난수 (nonce) 생성
    private String generateNonce() {
        byte[] randomBytes = new byte[8]; // 8바이트 난수 생성
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }
}
