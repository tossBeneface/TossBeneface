package com.app.domain.qnaboard.entity;

import com.app.domain.common.BaseEntity;
import com.app.domain.member.entity.Member;
import com.app.domain.qnaboard.constant.ContentStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QnaBoard extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long qnaBoardId;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private ContentStatus contentStatus;

    @OneToMany(mappedBy = "qnaBoard", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @OneToMany(mappedBy = "qnaBoard", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attachment> attachments;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId", nullable = false)
    private Member member;

    @Builder
    public QnaBoard(String title, String content, Member member, ContentStatus contentStatus, List<Attachment> attachments) {
        this.title = title;
        this.content = content;
        this.contentStatus = contentStatus;
        setMember(member); // 연관관계 편의 메서드 호출
        if (attachments != null) {
            attachments.forEach(this::addAttachment); // 연관관계 설정
        }
    }

    public void update(String newTitle, String newContent) {
        if (newTitle != null && !newTitle.isEmpty()) {
            this.title = newTitle;
        }
        if (newContent != null && !newContent.isEmpty()) {
            this.content = newContent;
        }
    }

    public void changeStatus(ContentStatus newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("상태값은 null일 수 없습니다.");
        }
        this.contentStatus = newStatus;
    }

    /**연관관계 편의 메서드*/
    public void setMember(Member member) {
        this.member = member;
        if (!member.getContents().contains(this)) {
            member.addQnaBoard(this); // Member 엔티티의 addQnaBoard 호출
        }
    }

    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setQnaBoard(this); // 양방향 연관 관계 설정
        // 작성자(Member)와의 관계도 설정
        if (comment.getMember() != null) {
            comment.getMember().addComment(comment);
        }
    }

    public void removeComment(Comment comment) {
        comments.remove(comment);
        comment.setQnaBoard(null); // 연관 관계 해제
    }

    public void addAttachment(Attachment attachment) {
        attachments.add(attachment);
        attachment.setQnaBoard(this); // 양방향 연관 관계 설정
    }

    public void removeAttachment(Attachment attachment) {
        attachments.remove(attachment);
        attachment.setQnaBoard(null); // 연관 관계 해제
    }

}
