//package com.app.api.card.service;
//
//import com.app.api.card.dto.OcrCardRequestDto;
//import com.app.api.card.dto.OcrCardResponseDto;
//import com.app.domain.card.entity.CardBin;
//import com.app.api.card.repository.CardBinRepository;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.*;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
//public class OcrCardService {
//
//    private final CardBinRepository cardBinRepository;
//
//    @Value("${fastapi.url}")
//    private String fastApiUrl;
//
//    public OcrCardResponseDto processCard(OcrCardRequestDto dto) {
//        // FastAPI 호출
//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//
//        HttpEntity<org.springframework.core.io.Resource> requestEntity =
//                new HttpEntity<>(dto.getImage().getResource(), headers);
//
//        ResponseEntity<String> responseEntity = restTemplate.postForEntity(fastApiUrl, requestEntity, String.class);
//        if (responseEntity.getStatusCode() != HttpStatus.OK) {
//            throw new RuntimeException("FastAPI 호출 실패");
//        }
//
//        // FastAPI 응답 데이터 처리
//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode responseBody;
//        try {
//            responseBody = objectMapper.readTree(responseEntity.getBody());
//        } catch (Exception e) {
//            throw new RuntimeException("FastAPI 응답 파싱 실패");
//        }
//
//        String cardNumber = responseBody.get("card_number").asText();
//        boolean cardDetected = responseBody.get("card_detected").asBoolean();
//        if (!cardDetected) {
//            return new OcrCardResponseDto(false, null, null, null, null, null, null, null);
//        }
//
//        // card_bin에서 card_id 조회
//        Optional<CardBin> cardBinEntityOptional = cardBinRepository.findByCardNumber(cardNumber);
//        if (cardBinEntityOptional.isEmpty()) {
//            throw new RuntimeException("카드 BIN 정보가 존재하지 않습니다.");
//        }
//
//        CardBin cardBinEntity = cardBinEntityOptional.get();
//        return new OcrCardResponseDto(
//                true,
//                cardNumber,
//                cardBinEntity.getCard().getCardName(),
//                cardBinEntity.getCard().getCardCompany(),
//                cardBinEntity.getCard().getCardImage(),
//                responseBody.get("date_info").asText(),
//                responseBody.get("cvc_info").asText(),
//                responseBody.get("other_text").asText()
//        );
//    }
//}
