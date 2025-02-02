package com.app.api.UserDataTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.app.domain.member.entity.Member;
import com.app.api.CardBenefit.CardBenefitEntity;
import com.app.domain.member.repository.MemberRepository;
import com.app.api.CardBenefit.CardBenefitRepository;

import java.util.List;
import java.util.Optional;

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
        if (userData.getCorcompany() == null || userData.getCard() == null) {
            return "❌ Corcompany and Card are required!";
        }
        Optional<CardBenefitEntity> cardBenefitOptional = cardBenefitRepository.findByCardNameAndCorp(
                userData.getCard(), userData.getCorcompany()
        );
        if (cardBenefitOptional.isEmpty()) {
            return "❌ Card Benefit not found!";
        }
        CardBenefitEntity cardBenefit = cardBenefitOptional.get();

        // 관계 설정
        userData.setMember(member);
        userData.setCardBenefit(cardBenefit);

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
}