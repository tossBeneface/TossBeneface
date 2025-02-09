//package com.app.api.card.controller;
//
//import com.app.api.card.dto.OcrCardRequestDto;
//import com.app.api.card.dto.OcrCardResponseDto;
//import com.app.api.card.service.OcrCardService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//@RestController
//@RequestMapping("/api/card-scan")
//@RequiredArgsConstructor
//public class OcrCardController {
//
//    private final OcrCardService ocrCardService;
//
//    @PostMapping
//    public ResponseEntity<OcrCardResponseDto> scanCard(@RequestParam("image") MultipartFile image) {
//        OcrCardRequestDto dto = new OcrCardRequestDto();
//        dto.setImage(image);
//
//        OcrCardResponseDto response = ocrCardService.processCard(dto);
//        return ResponseEntity.ok(response);
//    }
//}
