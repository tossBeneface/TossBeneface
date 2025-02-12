package com.app.api.UserDataTest.service;

import com.app.api.UserDataTest.UserDataTestRepository;
import com.app.api.UserDataTest.dto.UserCardRegisterDto;
import com.app.api.UserDataTest.dto.UserCardListDto;
import com.app.domain.card.entity.Card;
import com.app.api.UserDataTest.UserDataTestEntity;
import com.app.domain.member.entity.Member;
import com.app.domain.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class UserCardService {
    private final UserDataTestRepository repository;
    private final MemberRepository memberRepository;

    public UserCardService(UserDataTestRepository repository, MemberRepository memberRepository) {
        this.repository = repository;
        this.memberRepository = memberRepository;
    }

    //  회원의 카드 목록 조회 (@param memberId 회원 ID, @return 카드 목록
    public List<UserCardListDto> getUserCards(Long memberId) {
        return repository.findCardListByMemberId(memberId);
    }

    //  회원의 카드 등록 (@param dto 카드 등록 요청 DTO, @return 성공 메시지)
    @Transactional
    public String registerCard(UserCardRegisterDto dto, Long memberId) {
        // 1. card 테이블에서 card_name과 card_company로 카드 조회
        Optional<Card> cardOptional = repository.findCardByNameAndCompany(dto.getCardName(), dto.getCardCompany());
        if (cardOptional.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 카드입니다.");
        }

        // 2. memberId로 member 테이블 조회
        Optional<Member> memberOptional = memberRepository.findById(memberId);
        if (memberOptional.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 회원입니다. memberId: " + memberId);
        }


        // 3. 랜덤 값 생성 (실적, 혜택 등)
        Random random = new Random();
        int nowPer = random.nextInt(90001) * 10;
        int lastPer = (random.nextInt((100000 - (int)(nowPer / 10)) + 1) + (int)(nowPer / 10)) * 10;
        int cardLimit = (random.nextInt((1000000 - 100000) + 1) + 100000) * 10;
        int monthlySplit = random.nextInt(4);
        int accrueBenefit = (random.nextInt((5000 - 100) + 1) + 100) * 10;

        // 4. user_data_test 테이블에 저장
        UserDataTestEntity userDataTest = new UserDataTestEntity();
        userDataTest.setCard(cardOptional.get());
        userDataTest.setMember(memberOptional.get());
        userDataTest.setCardNumber(dto.getCardNumber());
        userDataTest.setExpiryDate(dto.getExpiryDate());
        userDataTest.setCvc(dto.getCvc());
        userDataTest.setPwd(dto.getPwd());
        userDataTest.setNowPer(nowPer);
        userDataTest.setLastPer(lastPer);
        userDataTest.setCardLimit(cardLimit);
        userDataTest.setMonthlySplit(monthlySplit);
        userDataTest.setAccrueBenefit(accrueBenefit);
        userDataTest.setCardCompany(dto.getCardCompany());
        userDataTest.setCardName(dto.getCardName());

        repository.save(userDataTest);
        return "카드 정보 저장 성공";
    }

    @Transactional
    public void deleteUserCard(Long cardId, Long memberId) {
        Optional<UserDataTestEntity> userCardOptional = repository.findCardByIdAndMemberId(cardId, memberId);

        if (userCardOptional.isEmpty()) {
            throw new IllegalArgumentException("삭제할 카드가 없거나 권한이 없습니다.");
        }

        repository.deleteCardByIdAndMemberId(cardId, memberId);
    }

}
