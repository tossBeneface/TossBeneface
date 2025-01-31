package com.app.api.flow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FlowDto {

    private String storeGenderScript;

    private String storeAgeScript;

    private String storeTimeScript;

    private String storeDayScript;

    private String districtGenderScript;

    private String districtAgeScript;

    private String districtTimeScript;

    private String districtDayScript;

//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createAt;
}
