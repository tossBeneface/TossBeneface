package com.app.api.faceRegisteration;

import jakarta.persistence.*;

@Entity
@Table(name = "face_registration")
public class FaceRegisterationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 사용자 이름(혹은 닉네임)
    @Column(name = "user_name", nullable = false)
    private String userName;

    // 실제 이미지 데이터 (BLOB)
    @Lob
    @Column(name = "image_data", nullable = false)
    private byte[] imageData;

    // 기본 생성자
    public FaceRegisterationEntity() {}

    // 생성자
    public FaceRegisterationEntity(String userName, byte[] imageData) {
        this.userName = userName;
        this.imageData = imageData;
    }

    // 게터/세터
    public Long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }
}