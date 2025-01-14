//package com.app;
//
//import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
//import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
//import org.junit.jupiter.api.Test;
//
//public class JasyptTest {
//
//    @Test
//    public void jasyptTest() {
//        String password = "sakncksjallkasdkl#$@^#*asdsiajodias2737";
//        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
//        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
//        encryptor.setPoolSize(4);
//        encryptor.setPassword(password);
//        encryptor.setAlgorithm("PBEWithHMACSHA512AndAES_256"); // 암호화 알고리즘 설정
//        config.setKeyObtentionIterations("1000"); // 키 스트레칭 반복 횟수
//        String content = "RwHsLoX0h6jH5/4TbISBOZWdMBxyIOAD1tjXyzazpoGErQg8hO1XNrAsNi3BLFQy4oDDCNZgOXEG5x1aKYS0Vw==";
//        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator"); // Salt 생성기
//        config.setStringOutputType("base64"); // 출력 타입 설정
//        encryptor.setConfig(config);
//
//        String encryptedContent = encryptor.encrypt(content); // 암호화
//        String decryptedContent = encryptor.decrypt(encryptedContent); // 복호화
//        System.out.println("Enc : " + encryptedContent + ", Dec : " + decryptedContent);
//
//    }
//}
