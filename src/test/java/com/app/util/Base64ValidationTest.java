package com.app.util;

import org.junit.jupiter.api.Test;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

public class Base64ValidationTest {

    @Test
    void validateBase64EncodedString() {
        // application.yml에 설정된 token.secret 값을 넣으세요.
        String tokenSecret = "RwHsLoX0h6jH5/4TbISBOZWdMBxyIOAD1tjXyzazpoGErQg8hO1XNrAsNi3BLFQy4oDDCNZgOXEG5x1aKYS0Vw==";

        try {
            // Base64 디코딩 시도
            byte[] decodedBytes = Base64.getDecoder().decode(tokenSecret);

            // 디코딩된 값의 길이 출력
            System.out.println("Decoded Length: " + decodedBytes.length);

            // 디코딩 성공 시 Assertion
            assertNotNull(decodedBytes, "Decoded value should not be null");
            assertTrue(decodedBytes.length >= 64, "Decoded length must be at least 64 bytes (Base64 encoded string of sufficient length).");
        } catch (IllegalArgumentException e) {
            // Base64 디코딩 실패 시 Assertion
            fail("The provided string is not a valid Base64 encoded string: " + e.getMessage());
        }
    }
}
