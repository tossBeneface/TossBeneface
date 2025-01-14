package com.app.global.config;

import com.app.global.error.FeignClientExceptionErrorDecoder;
import feign.Logger;
import feign.Logger.Level;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableFeignClients(basePackages = "com.app.api")
@ImportAutoConfiguration({FeignAutoConfiguration.class, HttpClientConfiguration.class})
public class FeignConfiguration {

    // 로깅 레벨 설정
    @Bean
    Logger.Level feignLoggerLevel() {
//        return Level.BASIC;
        return Level.FULL;
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new FeignClientExceptionErrorDecoder();
    }

    @Bean
    public Retryer retryer() {
        // 실행주가, Default의 nextMaxInterval에서 계산됨, 최대 몇번의 재시도를 할지
        return new Retryer.Default(1000, 2000, 3);
    }


}
