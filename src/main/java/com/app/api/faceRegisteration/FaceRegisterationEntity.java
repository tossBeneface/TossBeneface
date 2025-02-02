package com.app.api.faceRegisteration;

import jakarta.persistence.*;
import com.app.domain.member.entity.Member;

@Entity
@Table(name = "face_registration")
public class FaceRegisterationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Member 엔티티와 외래키 관계 설정 (1명의 Member가 여러 개의 얼굴 등록 가능)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

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
    public FaceRegisterationEntity(Member member, String userName, String imageUrl) {
        this.member = member;
        this.userName = userName;
        this.imageUrl = imageUrl;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public String getUserName() {
        return userName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}