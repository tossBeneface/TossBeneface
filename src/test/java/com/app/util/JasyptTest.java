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
        config.setPoolSize(4);
//        config.setAlgorithm("PBEWithHMACSHA512AndAES_256"); // 암호화 알고리즘 설정
        config.setAlgorithm("PBEWithMD5AndTripleDES"); // 암호화 알고리즘 설정
//        config.setAlgorithm("PBEWithMD5AndDES"); // 암호화 알고리즘 설정
        config.setPassword(password); // 암호화 비밀번호 설정
        config.setKeyObtentionIterations("1000"); // 키 스트레칭 반복 횟수 설정
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator"); // Salt 생성기 설정
        config.setStringOutputType("base64"); // 출력 타입 설정
        config.setProviderName("SunJCE");

        encryptor.setConfig(config);

        // 암호화 및 복호화 대상
        String awsSecretKey = "G/tPFbXWO12zHZYUzOw76O2DqWOvo1J++cvlJYUI";
        String content = "RwHsLoX0h6jH5/4TbISBOZWdMBxyIOAD1tjXyzazpoGErQg8hO1XNrAsNi3BLFQy4oDDCNZgOXEG5x1aKYS0Vw==";
        String DBpassword = "toss10jodev";
        String tossTestClientApiKey = "test_ck_DpexMgkW36ya40vyX0l48GbR5ozO";
        String tossTestSecretApiKey = "test_sk_GePWvyJnrKQdQ5Ey5OQ63gLzN97E";

        // Jwt token secret
        /*
        String encryptedContent = encryptor.encrypt(content); // 암호화
        String decryptedContent = encryptor.decrypt(encryptedContent); // 복호화
        System.out.println("Enc : " + encryptedContent + ", Dec : " + decryptedContent);

        // AWS Secret Key
        String encryptedSecretKey = encryptor.encrypt(awsSecretKey); // 암호화
        String decryptedSecretKey = encryptor.decrypt(encryptedSecretKey); // 복호화
        System.out.println("Enc : " + encryptedSecretKey + ", Dec : " + decryptedSecretKey);


        // DB Password
        String encryptedDBpassword = encryptor.encrypt(DBpassword); // 암호화
        String decryptedDBPassword = encryptor.decrypt(encryptedDBpassword); // 복호화
        System.out.println("Enc : " + encryptedDBpassword + ", Dec : " + decryptedDBPassword);
 */
        // Toss Paymets tossTestClientApiKey
        String encryptedAPIKey = encryptor.encrypt(tossTestClientApiKey); // 암호화
        String decryptedAPIKey = encryptor.decrypt(encryptedAPIKey); // 복호화
        System.out.println("Enc : " + encryptedAPIKey + ", Dec : " + decryptedAPIKey);

        // tossTestSecretApiKey
        String encryptedSecretAPIKey = encryptor.encrypt(tossTestSecretApiKey); // 암호화
        String decryptedSecretAPIKey = encryptor.decrypt(encryptedSecretAPIKey); // 복호화
        System.out.println("Enc : " + encryptedSecretAPIKey + ", Dec : " + decryptedSecretAPIKey);

        // 결과 검증
        /*
        assert content.equals(decryptedContent) : "복호화된 값이 원본과 다릅니다!";
        assert awsSecretKey.equals(decryptedSecretKey) : "복호화된 값이 원본과 다릅니다!";
        assert DBpassword.equals(decryptedDBPassword) : "복호화된 값이 원본과 다릅니다!";
        */
        assert tossTestClientApiKey.equals(decryptedAPIKey) : "복호화된 값이 원본과 다릅니다!";
        assert tossTestSecretApiKey.equals(decryptedSecretAPIKey) : "복호화된 값이 원본과 다릅니다!";
    }
}
