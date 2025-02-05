package com.app.api.fastapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class HospitalResponse {
    private String hospitalName;
    private String address;
    private String emergencyMedicalInstitutionType;
    private String phoneNumber1;
    private String phoneNumber3;
    private String request;
    private double distance;

}



