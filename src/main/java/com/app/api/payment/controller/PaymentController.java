package com.app.api.payment.controller;

import com.app.api.payment.dto.PaymentDto;
import com.app.api.payment.dto.PaymentFailDto;
import com.app.api.payment.dto.PaymentResDto;
import com.app.api.payment.service.PaymentServiceImpl;
import com.app.domain.member.entity.Member;
import com.app.global.config.TossPaymentConfig;
import com.app.global.resolver.memberInfo.MemberInfo;
import com.app.global.resolver.memberInfo.MemberInfoDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.bouncycastle.asn1.ocsp.SingleResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
public class PaymentController {
    private final PaymentServiceImpl paymentService;
    private final TossPaymentConfig tossPaymentConfig;

    // 결제 요청 처리 (POST)
    @PostMapping("/toss")
    public ResponseEntity requestTossPayment(
            @AuthenticationPrincipal Member principal,
            @RequestBody @Valid PaymentDto paymentReqDto
    ) {
        System.out.println("🔹 결제 요청 API 호출됨 - memberId: {}, amount: {}"
        ); // ✅ 로그 추가

        // 결제 요청 처리
        PaymentResDto paymentResDto = paymentService.requestTossPayment(
                paymentReqDto.toEntity(), principal.getMemberId()
        ).toPaymentResDto();

        // 결제 성공/실패 URL 설정
        paymentResDto.setSuccessUrl(paymentReqDto.getYourSuccessUrl() != null
                ? paymentReqDto.getYourSuccessUrl() : tossPaymentConfig.getSuccessUrl());
        paymentResDto.setFailUrl(paymentReqDto.getYourFailUrl() != null
                ? paymentReqDto.getYourFailUrl() : tossPaymentConfig.getFailUrl());

        log.info("✅ 결제 요청 처리 완료 - paymentResDto: {}", paymentResDto);

        return ResponseEntity.ok().body(paymentResDto);
    }


    // 결제 성공 처리 (GET)
    @GetMapping("/toss/success")
    public ResponseEntity tossPaymentSuccess(
            @RequestParam String paymentKey,
            @RequestParam String orderId,
            @RequestParam Long amount
    ) {

        // 결제 성공 처리
        paymentService.tossPaymentSuccess(paymentKey, orderId, amount);

        return ResponseEntity.ok().body("결제 성공");
    }

    // 결제 실패 처리 (GET)
    @GetMapping("/toss/fail")
    public ResponseEntity tossPaymentFail(
            @RequestParam String code,
            @RequestParam String message,
            @RequestParam String orderId
    ) {
        // 결제 실패 처리
        paymentService.tossPaymentFail(code, message, orderId);

        return ResponseEntity.ok().body(
                PaymentFailDto.builder()
                        .errorCode(code)
                        .errorMessage(message)
                        .orderId(orderId)
                        .build()
        );
    }
}

