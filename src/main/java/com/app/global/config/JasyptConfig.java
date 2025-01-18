package com.app.global.config;

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** 설정 정보 암호화 및 복호화(DB비번, API키, 토큰 등)
 *
 */
@Configuration
public class JasyptConfig {

    @Value("${jasypt.password}")
    private String password;

    @Bean
    public PooledPBEStringEncryptor jasyptStringEncryptor() {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();

        config.setPoolSize(4);
//        config.setAlgorithm("PBEWithHMACSHA512AndAES_256"); // 암호화 알고리즘 설정
        config.setAlgorithm("PBEWithMD5AndTripleDES"); // 암호화 알고리즘 설정
//        config.setAlgorithm("PBEWithMD5AndDES"); // 암호화 알고리즘 설정
        config.setPassword(password); // 암호화 비밀번호 설정
        config.setKeyObtentionIterations("1000"); // 키 스트레칭 반복 횟수 설정
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator"); // Salt 생성기 설정
        config.setStringOutputType("base64"); // 출력 타입 설정
        config.setProviderName("SunJCE");

        encryptor.setConfig(config); // 설정을 encryptor에 전달
        return encryptor;
    }
}
