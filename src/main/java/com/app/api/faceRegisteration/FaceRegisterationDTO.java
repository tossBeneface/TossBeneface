package com.app.api.faceRegisteration;

import com.app.api.faceRegisteration.FaceRegisterationEntity;

public class FaceRegisterationDTO {
    private Long id;
    private String userName;
    private String imageUrl;
    private Long memberId; // 🔥 Lazy Loading 문제 방지 (member 전체를 불러오지 않고, ID만 저장)

    // 🔹 Entity -> DTO 변환 생성자
    public FaceRegisterationDTO(FaceRegisterationEntity entity) {
        this.id = entity.getId();
        this.userName = entity.getUserName();
        this.imageUrl = entity.getImageUrl();
        this.memberId = entity.getMember().getMemberId(); // 🔥 member의 모든 데이터 대신, ID만 저장
    }

    // 🔹 Getters (Setter는 필요하지 않음, DTO는 Immutable하게 유지)
    public Long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Long getMemberId() {
        return memberId;
    }
}