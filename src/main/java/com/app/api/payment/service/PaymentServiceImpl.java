package com.app.api.payment.service;

import com.app.api.payment.dto.PaymentSuccessDto;
import com.app.api.payment.repository.PaymentRepository;
import com.app.domain.member.entity.Member;
import com.app.domain.member.repository.MemberRepository;
import com.app.domain.payment.entity.Payment;
import com.app.global.config.TossPaymentConfig;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final MemberRepository memberRepository;
    private final PaymentRepository paymentRepository;
    private final TossPaymentConfig tossPaymentConfig;
    private final EntityManager entityManager; // EntityManager 주입

    @Transactional
    public Payment requestTossPayment(Payment payment, Long memberId) {
        log.info("🔹 requestTossPayment 시작 - memberId: {}, amount: {}", memberId, payment.getAmount()); // ✅ 추가

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 유저가 없습니다."));

        if (payment.getAmount() < 1000) {
            throw new IllegalArgumentException("결제 금액이 너무 적습니다.");
        }

        payment.setCustomer(member);
        Payment savedPayment = paymentRepository.save(payment);

        log.info("✅ 결제 저장 완료 - orderId: {}, amount: {}", savedPayment.getOrderId(), savedPayment.getAmount());

        return savedPayment;
    }


    @Transactional
    public PaymentSuccessDto tossPaymentSuccess(String paymentKey, String orderId, Long amount) {
        Payment payment = verifyPayment(orderId, amount);
        PaymentSuccessDto result = requestPaymentAccept(paymentKey, orderId, amount);
        payment.setPaymentKey(paymentKey);
        payment.setPaySuccessYN(true);

        // 현재 세션에서 Member 엔티티를 다시 로드하여 세션에 연결
        Member customer = memberRepository.findById(payment.getCustomer().getMemberId()).orElseThrow(() ->
                new IllegalArgumentException("고객을 찾을 수 없습니다"));

        customer.setBudget((customer.getBudget() + amount));

        // EntityManager.merge()로 영속화 컨텍스트에 병합
        entityManager.merge(customer); // Merge 사용하여 detached 엔티티 병합

        // 이제 save와 flush는 필요 없고, 병합된 엔티티는 자동으로 영속화 컨텍스트에 반영됩니다.
        memberRepository.updateBudget(customer.getMemberId(), Math.toIntExact(payment.getAmount()));

        return result;
    }

    public Payment verifyPayment(String orderId, Long amount) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문이 없습니다"));

        if (!payment.getAmount().equals(amount)) {
            throw new IllegalArgumentException("금액이 일치하지 않습니다");
        }
        return payment;
    }

    @Transactional
    public PaymentSuccessDto requestPaymentAccept(String paymentKey, String orderId, Long amount) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = getHeaders();
        JSONObject params = new JSONObject();
        params.put("orderId", orderId);
        params.put("amount", amount);

        PaymentSuccessDto result = null;
        try {
            result = restTemplate.postForObject(TossPaymentConfig.URL + paymentKey,
                    new HttpEntity<>(params, headers),
                    PaymentSuccessDto.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("");
        }

        return result;
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        String encodedAuthKey = new String(
                Base64.getEncoder().encode((tossPaymentConfig.getTestSecretKey() + ":").getBytes(StandardCharsets.UTF_8)));
        headers.setBasicAuth(encodedAuthKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }

    @Transactional
    public void tossPaymentFail(String code, String message, String orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId).orElseThrow(() -> {
            throw new IllegalArgumentException("");
        });
        payment.setPaySuccessYN(false);
        payment.setFailReason(message);
    }
}
