package com.app.api.UserDataTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-data-test")
public class UserDataTestController {

    @Autowired
    private UserDataTestRepository repository;

    @PostMapping
    public String saveUserData(@RequestBody UserDataTestEntity userData) {
        repository.save(userData);
        return "데이터가 성공적으로 저장되었습니다!";
    }

    @GetMapping
    public List<UserDataTestEntity> getUserDataByUsername(@RequestParam(required = false) String username) {
        if (username != null && !username.isEmpty()) {
            // username으로 데이터 필터링
            return repository.findByUsername(username);
        } else {
            // username이 없으면 전체 데이터 반환
            return repository.findAll();
        }
    }
}