package com.app.api.payment.controller;

import com.app.domain.member.entity.Member;
import com.app.domain.member.service.MemberService;
import com.app.global.resolver.memberInfo.MemberInfo;
import com.app.global.resolver.memberInfo.MemberInfoDto;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;  // Import org.json.JSONObject instead of JSONParser
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@RestController
@RequiredArgsConstructor
public class WidgetController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final MemberService memberService;

    @RequestMapping(value = "/confirm", method = RequestMethod.POST)
    public ResponseEntity<JSONObject> confirmPayment(@MemberInfo MemberInfoDto memberInfoDto, @RequestBody String jsonBody) throws Exception {

        String orderId;
        String amount;
        String paymentKey;

        // JSON 데이터 파싱
        JSONObject requestData = new JSONObject(jsonBody); // Directly create a JSONObject from the body
        paymentKey = requestData.getString("paymentKey");
        orderId = requestData.getString("orderId");
        amount = requestData.getString("amount");

        // 결제 확인 요청 객체 생성
        JSONObject obj = new JSONObject();
        obj.put("orderId", orderId);
        obj.put("amount", amount);
        obj.put("paymentKey", paymentKey);

        // 기존의 SecretKey가 아닌, memberInfoDto로부터 JWT 토큰을 받아 Authorization 헤더에 추가
        String token = "eyJ0eXBlIjoiSldUIiwiYWxnIjoiSFM1MTIifQ.eyJzdWIiOiJBQ0NFU1MiLCJpYXQiOjE3MzgyNTQ5MTgsImV4cCI6MTczODI1NTgxOCwibWVtYmVySWQiOjEsInJvbGUiOiJVU0VSIn0.y_url_3pu0-O32eCaXCOue0Sr7essFPM2NuF05x5JwF9X0EkXcOz0cqKXZg19KzKIEthAS6ROoA4Xce-LpbU6Q"; // memberInfoDto에서 JWT 토큰 추출 (여기서 토큰을 가져오세요)
        if (token == null || token.isEmpty()) {
            throw new Exception("JWT 토큰이 없습니다.");
        }

        // Authorization 헤더에 JWT 토큰을 Bearer 방식으로 추가
        String authorizations = "Bearer " + token;

        logger.info("Authorization header: {}", authorizations);

        URL url = new URL("https://api.tosspayments.com/v1/payments/confirm");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Authorization", authorizations);  // Bearer 토큰 추가
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(obj.toString().getBytes("UTF-8"));

        int code = connection.getResponseCode();
        logger.info("Response Code: {}", code); // 응답 코드 로그 출력
        boolean isSuccess = code == 200;

        InputStream responseStream = isSuccess ? connection.getInputStream() : connection.getErrorStream();
        Reader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8);
        JSONObject jsonObject = new JSONObject(new JSONTokener(reader)); // Parse the response
        responseStream.close();

        // 결제 성공 처리
        if (isSuccess) {
            // memberInfoDto 사용: 이미 로그인된 사용자의 정보를 기반으로 결제 처리
            String memberEmail = memberInfoDto.getEmail();  // memberInfoDto에서 이메일 추출
            Member member = memberService.getMemberByEmail(memberEmail);

            if (member != null) {
                int paymentAmount = Integer.parseInt(amount);
//                int updatedBudget = member.getBudget() - paymentAmount;
//                member.setBudget(updatedBudget);

                memberService.updateMember(member);
//                logger.info("Updated budget for member {}: {}", memberEmail, updatedBudget);
            }
        } else {
            // 결제 실패 처리 (isSuccess == false)
            logger.error("Payment confirmation failed for orderId: {}. Response: {}", orderId, jsonObject.toString());
            return ResponseEntity.status(code).body(jsonObject); // 실패한 응답 반환
        }

        return ResponseEntity.status(code).body(jsonObject); // 성공 또는 실패 응답 반환
    }

}


