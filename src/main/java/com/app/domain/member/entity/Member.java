package com.app.domain.member.entity;

import com.app.domain.common.BaseEntity;
import com.app.domain.member.constant.Gender;
import com.app.domain.member.constant.MemberStatus;
import com.app.domain.member.constant.Role;
import com.app.domain.qnaboard.entity.Comment;
import com.app.domain.qnaboard.entity.QnaBoard;
import com.app.global.jwt.dto.JwtTokenDto;
import com.app.global.util.DateTimeUtils;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    // 토스 가입창 : 성명, 이메일 주소, 전화번호, 비밀번호
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본키 생성 전략을 데이터베이스에 맡기겠다
    private Long memberId;

    @Column(unique = true, length = 50, nullable = false)
    private String email;

    @Column(nullable = false, length = 200)
    private String password;

    @Column(nullable = false, length = 20)
    private String memberName;

    @Column(nullable = false, length = 20)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Gender gender;

    // 토스 회원가입시X, 필요데이터
    @Column(nullable = true, length = 200)
    private String profileImg;

//    @Column(nullable = true, length = 200)
    @Column(nullable = true, columnDefinition = "INT DEFAULT 1000000")
    private Integer budget = 10000000;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 25) // TEMPORARILY_SUSPENDED 넣고싶을때를 고려
    private MemberStatus memberStatus;

    @Column(length = 250)
    private String refreshToken;

    // 게시판 글
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QnaBoard> contents;

    // 게시판 댓글
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    private LocalDateTime tokenExpirationTime;

    // 필요한 필드들만 빌더로 받아서 사용할 수 있게
    @Builder
    public Member(String email, String password, String memberName, String phoneNumber, Gender gender,
        String profileImg, Integer budget, Role role, MemberStatus memberStatus) {

        this.email = email;
        this.password = password;
        this.memberName = memberName;
        this.phoneNumber = phoneNumber;
        this.profileImg = profileImg;
        this.budget = (budget != null) ? budget : 10000000;
        this.role = role;
        this.gender = gender;
        this.memberStatus = memberStatus;
    }

    /**연관관계 편의 메서드*/
    public void addQnaBoard(QnaBoard qnaBoard) {
        contents.add(qnaBoard);
        qnaBoard.setMember(this);
    }

    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setMember(this);
    }

    public void updateRefreshToken(JwtTokenDto jwtTokenDto) {
        this.refreshToken = jwtTokenDto.getRefreshToken();
        this.tokenExpirationTime = DateTimeUtils.convertToLocalDateTime(jwtTokenDto.getRefreshTokenExpireTime());
    }

    public void expireRefreshToken(LocalDateTime now) {
        this.tokenExpirationTime = now;
    }
}
