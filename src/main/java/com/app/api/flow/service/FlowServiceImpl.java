package com.app.api.flow.service;

import com.app.api.flow.dto.FlowDto;
import com.app.api.flow.repository.FlowRepository;
import com.app.domain.districtAnaly.entity.Flow;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FlowServiceImpl implements FlowService {
    private final FlowRepository flowRepository;

    @Transactional(readOnly = true)
    public FlowDto getFlow() {
        Flow flow = flowRepository.findTopByOrderByCreatedAtDesc()
                .orElseThrow(() -> new IllegalArgumentException("데이터가 존재하지 않습니다."));

        return FlowDto.builder()
                .storeGenderScript(flow.getStoreAgeScript())
                .storeAgeScript(flow.getStoreAgeScript())
                .storeDayScript(flow.getStoreDayScript())
                .storeTimeScript(flow.getStoreTimeScript())
                .districtGenderScript(flow.getDistrictGenderScript())
                .districtAgeScript(flow.getDistrictAgeScript())
                .districtDayScript(flow.getDistrictDayScript())
                .districtTimeScript(flow.getDistrictTimeScript())
                .createAt(flow.getCreatedAt())
                .build();

    }
}
