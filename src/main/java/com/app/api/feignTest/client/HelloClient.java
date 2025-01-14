package com.app.api.feignTest.client;

import com.app.api.health.dto.HealthCheckResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "helloClient", url = "http://localhost:8080")
public interface HelloClient {

    @GetMapping(value = "/api/health", consumes = "application/json")
    HealthCheckResponseDto healthCheck();

    // 에러 테스트
//    @GetMapping(value = "/api/health2", consumes = "application/json")
//    HealthCheckResponseDto healthCheck();
    // healthcheck api의 호출 결과였다. 이것을 반환해 주도록 하겠다.

}
