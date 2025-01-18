package com.app.util;

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.junit.jupiter.api.Test;

public class JasyptTest {

    @Test
    public void jasyptTest() {
        String password = "sakncksjallkasdkl#$@^#*asdsiajodias2737";
        // Jasypt Encryptor 생성 및 설정
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();

        // encryptor와 config 설정
//        encryptor.setPoolSize(4); // Pooled Encryptor 설정
//        encryptor.setPassword(password); // 암호화 비밀번호 설정
        config.setPoolSize(4);
//        config.setAlgorithm("PBEWithHMACSHA512AndAES_256"); // 암호화 알고리즘 설정
        config.setAlgorithm("PBEWithMD5AndDES"); // 암호화 알고리즘 설정
        config.setPassword(password); // 암호화 비밀번호 설정
        config.setKeyObtentionIterations("1000"); // 키 스트레칭 반복 횟수 설정
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator"); // Salt 생성기 설정
        config.setStringOutputType("base64"); // 출력 타입 설정
        config.setProviderName("SunJCE");

        encryptor.setConfig(config);

        // 암호화 및 복호화 대상
        String awsSecretKey = "G/tPFbXWO12zHZYUzOw76O2DqWOvo1J++cvlJYUI";
        String content = "RwHsLoX0h6jH5/4TbISBOZWdMBxyIOAD1tjXyzazpoGErQg8hO1XNrAsNi3BLFQy4oDDCNZgOXEG5x1aKYS0Vw==";

        // Jwt token secret
        String encryptedContent = encryptor.encrypt(content); // 암호화
        String decryptedContent = encryptor.decrypt(encryptedContent); // 복호화
        System.out.println("Enc : " + encryptedContent + ", Dec : " + decryptedContent);


        // AWS Secret Key
        String encryptedSecretKey = encryptor.encrypt(awsSecretKey); // 암호화
        String decryptedSecretKey = encryptor.decrypt(encryptedSecretKey); // 복호화
        System.out.println("Enc : " + encryptedSecretKey + ", Dec : " + decryptedSecretKey);

        // 결과 검증
        assert content.equals(decryptedContent) : "복호화된 값이 원본과 다릅니다!";
        assert awsSecretKey.equals(decryptedSecretKey) : "복호화된 값이 원본과 다릅니다!";

    }
}
