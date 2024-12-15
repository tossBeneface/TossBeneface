package com.app.api.health.controller;

import com.app.api.health.dto.HealthCheckResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;

import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "health check", description = "서버 상태 체크 API")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class HealthCheckController {

    private final Environment environment;

    @Tag(name = "health check")
    @Operation(summary = "서버 Health Check API", description = "현재 서버가 정상적으로 기동이 된 상태인지 검사하는 API")
    @GetMapping("/health")
    public ResponseEntity<HealthCheckResponseDto> healthCheck() {

        // 에러 테스트 : 리드 타임 아웃 발생시키기
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        HealthCheckResponseDto healthCheckResponseDto = HealthCheckResponseDto.builder()
            .health("ok")
            .activeProfiles(Arrays.asList(environment.getActiveProfiles()))
            .build();
        return ResponseEntity.ok(healthCheckResponseDto);
    }
}
