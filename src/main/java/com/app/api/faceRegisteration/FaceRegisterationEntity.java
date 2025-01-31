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

    // S3 업로드 후 반환되는 파일 URL (혹은 오브젝트 키)
    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    // 기본 생성자
    public FaceRegisterationEntity() {
    }

    // 생성자
    public FaceRegisterationEntity(String userName, String imageUrl) {
        this.userName = userName;
        this.imageUrl = imageUrl;
    }

    // getters, setters
    public Long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}