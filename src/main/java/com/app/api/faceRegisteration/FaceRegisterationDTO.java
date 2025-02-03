package com.app.api.faceRegisteration;

import com.app.api.faceRegisteration.FaceRegisterationEntity;

public class FaceRegisterationDTO {
    private Long id;
    private String imageUrl;
    private Long memberId; // ✅ Lazy Loading 문제 방지

    public FaceRegisterationDTO(FaceRegisterationEntity entity) {
        this.id = entity.getId();
        this.imageUrl = entity.getImageUrl();

        // ✅ Lazy Loading 문제 방지: `memberId`만 저장
        this.memberId = (entity.getMember() != null) ?
                (entity.getMember().getMemberId() != null ? entity.getMember().getMemberId() : null)
                : null;
    }

    public Long getId() {
        return id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Long getMemberId() {
        return memberId;
    }
}