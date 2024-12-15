package com.app.api.feignTest.controller;

import com.app.api.feignTest.client.HelloClient;
import com.app.api.health.dto.HealthCheckResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// todo test패키지들은 테스트를 위해 생성한 것이니 템플릿 프로젝트로 깃헙에 등록할 때는 제외시키기
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class HealthFeignTestController {

    private final HelloClient helloClient;

    @GetMapping("/health/feign-test")
    public ResponseEntity<HealthCheckResponseDto> healthCheckTest() {
        HealthCheckResponseDto healthCheckResponseDto = helloClient.healthCheck();
        return ResponseEntity.ok(healthCheckResponseDto);
    }

}
