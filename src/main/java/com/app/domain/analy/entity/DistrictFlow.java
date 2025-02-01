package com.app.domain.analy.entity;

import com.app.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DistrictFlow extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String storeGenderScript;

    @Column
    private String storeAgeScript;

    @Column
    private String storeTimeScript;

    @Column
    private String storeDayScript;

    @Column
    private String districtGenderScript;

    @Column
    private String districtAgeScript;

    @Column
    private String districtTimeScript;

    @Column
    private String districtDayScript;


}
