package com.app.api.UserDataTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.app.domain.member.entity.Member;
import com.app.api.CardBenefit.CardBenefitEntity;
import com.app.domain.member.repository.MemberRepository;
import com.app.api.CardBenefit.CardBenefitRepository;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user-data-test")
public class UserDataTestController {

    @Autowired
    private UserDataTestRepository userDataTestRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CardBenefitRepository cardBenefitRepository;

    // ✅ POST 요청: UserData 저장 (corcompany 및 cardName 기반으로 저장)
    @PostMapping
    public String saveUserData(@RequestBody UserDataTestEntity userData) {
        // Member 존재 여부 확인
        if (userData.getMember() == null || userData.getMember().getMemberId() == null) {
            return "❌ Member ID is required!";
        }
        Optional<Member> memberOptional = memberRepository.findById(userData.getMember().getMemberId());
        if (memberOptional.isEmpty()) {
            return "❌ Member not found!";
        }
        Member member = memberOptional.get();

        // ✅ `cardBenefitId` 없이 `corcompany` + `card` 기반으로 조회
        if (userData.getCardCompany() == null || userData.getCard() == null) {
            return "❌ CardCompany and CardName are required!";
        }
        // Repository가 여러 결과를 반환하도록 수정되어 있으므로 List로 받아야 합니다.
        List<CardBenefitEntity> cardBenefitList = cardBenefitRepository.findByCardNameAndCardCompany(
                userData.getCardName(), userData.getCardCompany()
        );
        if (cardBenefitList.isEmpty()) {
            return "❌ Card Benefit not found!";
        }
        // 여러 결과 중 첫 번째 결과를 사용 (필요에 따라 다른 로직을 적용할 수 있음)
        CardBenefitEntity cardBenefit = cardBenefitList.get(0);

        // 관계 설정
        userData.setMember(member);
//        userData.setCardBenefit(cardBenefit);

        // 저장
        userDataTestRepository.save(userData);
        return "✅ UserDataTestEntity saved successfully!";
    }

    // ✅ GET 요청: 특정 회원(memberId)의 보유 카드 목록 조회 (corcompany + card 조합)
    @GetMapping("/cards")
    public List<String> getCardsByMemberId(@RequestParam Long memberId) {
        List<Object[]> cardDetails = userDataTestRepository.findCardDetailsByMemberId(memberId);

        // 카드 이름과 회사 정보를 하나의 문자열로 변환 (예: "삼성카드 - 삼성")
        List<String> cardList = cardDetails.stream()
                .map(card -> card[0] + " - " + card[1])  // card + corcompany 조합
                .toList();

        return cardList;
    }

    // ✅ GET 요청: 특정 회원(memberId)의 카드별 혜택 정보 조회 (corcompany + card 기준)
    @GetMapping("/card-benefits")
    public List<CardBenefitEntity> getCardBenefitsByMemberId(@RequestParam Long memberId) {
        return userDataTestRepository.findCardBenefitsByMemberId(memberId);
    }

    // ✅ 추가된 API: 특정 회원(memberId)의 재무 관련 데이터(last_per, pay_amount, monthly_split, now_per, Accrue_benefit) 조회
    @GetMapping("/financial-data")
    public List<Map<String, Object>> getFinancialDataByMemberId(@RequestParam Long memberId) {
        // Repository에서 7개 컬럼(card, corcompany, lastPer, payAmount, monthlySplit, nowPer, accrueBenefit)을 조회
        List<Object[]> results = userDataTestRepository.findFinancialDataByMemberId(memberId);
        List<Map<String, Object>> response = new ArrayList<>();

        // 각 행을 Map으로 변환
        for (Object[] row : results) {
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("card_name", row[0]);
            dataMap.put("card_company", row[1]);
            dataMap.put("last_per", row[2]);
            dataMap.put("pay_amount", row[3]);
            dataMap.put("monthly_split", row[4]);
            dataMap.put("now_per", row[5]);
            dataMap.put("Accrue_benefit", row[6]);
            response.add(dataMap);
        }
        return response;
    }
}