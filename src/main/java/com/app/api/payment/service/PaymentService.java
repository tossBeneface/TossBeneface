package com.app.api.payment.service;

import com.app.api.card.repository.CardRepository;
import com.app.api.payment.repository.PaymentRepository;
import com.app.domain.card.entity.Card;
import com.app.domain.payment.entity.Payment;
import com.app.domain.member.entity.Member;
import com.app.domain.member.repository.MemberRepository;
import com.app.global.resolver.memberInfo.MemberInfoDto;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private MemberRepository memberRepository;
    private CardRepository cardRepository;

    @Transactional
    public void savePayment(JSONObject response, MemberInfoDto memberInfo) {
        // 1. MEMBER ID 가져오기 (response 또는 memberInfo에서)
        Long memberId = response.has("memberId") ? response.getLong("memberId") : memberInfo.getMemberId();

        // 2. MEMBER 정보 가져오기
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        // 3. Payment 정보 저장
        Payment payment = new Payment();
        payment.setPaymentKey(response.getString("paymentKey"));
        payment.setOrderId(response.getString("orderId"));
        payment.setOrderName(response.getString("orderName"));
        payment.setMethod(response.getString("method"));
        payment.setTotalAmount(response.getInt("totalAmount"));
        payment.setStatus(response.getString("status"));
        payment.setRequestedAt(response.getString("requestedAt"));
        payment.setApprovedAt(response.getString("approvedAt"));
        payment.setReceiptUrl(response.getJSONObject("receipt").getString("url"));
        payment.setMember(member); // ✅ 올바르게 Member 설정

        // 4. 결제 정보 저장
        paymentRepository.save(payment);

        // 5. MEMBER의 Budget 차감 (기본값 "0" 처리)
        Integer budget = Optional.ofNullable(member.getBudget())
                .orElse(0); // null이면 기본값 0 할당


        Integer newBudget = budget - payment.getTotalAmount();

        if (newBudget < 0) {
            throw new RuntimeException("Insufficient funds");
        }
        member.setBudget(newBudget);

        // 6. MEMBER 정보 업데이트
        memberRepository.save(member);
    }

}
