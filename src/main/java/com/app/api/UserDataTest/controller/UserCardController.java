package com.app.api.UserDataTest.controller;

import com.app.api.UserDataTest.dto.UserCardListDto;
import com.app.api.UserDataTest.dto.UserCardRegisterDto;
import com.app.api.UserDataTest.service.UserCardService;
import com.app.global.resolver.memberInfo.MemberInfo;
import com.app.global.resolver.memberInfo.MemberInfoDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-cards")
public class UserCardController {

    private final UserCardService userCardService;

    public UserCardController(UserCardService userCardService) {
        this.userCardService = userCardService;
    }

    //  회원의 카드 목록을 조회하는 API (@param memberId 회원 ID, @return 회원이 보유한 카드 목록)
    @GetMapping
    public ResponseEntity<List<UserCardListDto>> getUserCards(@MemberInfo MemberInfoDto memberInfoDto) {
        List<UserCardListDto> userCards = userCardService.getUserCards(memberInfoDto.getMemberId());
        return ResponseEntity.ok(userCards);
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerCard(
            @RequestBody UserCardRegisterDto dto,
            @MemberInfo MemberInfoDto memberInfoDto) {
        return ResponseEntity.ok(userCardService.registerCard(dto, memberInfoDto.getMemberId()));
    }
    @DeleteMapping("/{cardId}")
    public ResponseEntity<String> deleteUserCard(
            @PathVariable Long cardId,
            @MemberInfo MemberInfoDto memberInfoDto) {
        userCardService.deleteUserCard(cardId, memberInfoDto.getMemberId());
        return ResponseEntity.ok("카드 삭제 완료");
    }
}
