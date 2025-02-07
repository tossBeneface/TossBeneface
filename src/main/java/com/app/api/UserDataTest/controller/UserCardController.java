package com.app.api.UserDataTest.controller;

import com.app.api.UserDataTest.dto.UserCardListDto;
import com.app.api.UserDataTest.dto.UserCardRegisterDto;
import com.app.api.UserDataTest.service.UserCardService;
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
    @GetMapping("/{memberId}")
    public ResponseEntity<List<UserCardListDto>> getUserCards(@PathVariable Long memberId) {
        List<UserCardListDto> userCards = userCardService.getUserCards(memberId);
        return ResponseEntity.ok(userCards);
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerCard(@RequestBody UserCardRegisterDto dto) {
        return ResponseEntity.ok(userCardService.registerCard(dto));
    }
}
