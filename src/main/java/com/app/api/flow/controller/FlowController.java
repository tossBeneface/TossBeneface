package com.app.api.flow.controller;

import com.app.api.flow.dto.FlowDto;
import com.app.api.flow.service.FlowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class FlowController {
    private final FlowService flowService;

    @GetMapping("/flow")
    public ResponseEntity<FlowDto> getFlow(
    ) {
     FlowDto flowDto = flowService.getFlow();
     return ResponseEntity.status(HttpStatus.OK).body(flowDto);
    }
}
