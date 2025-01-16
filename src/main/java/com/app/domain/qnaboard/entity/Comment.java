package com.app.domain.qnaboard.entity;

import com.app.domain.member.entity.Member;
import com.app.domain.qnaboard.constant.CommentStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String commentContent;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private CommentStatus commentStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "qnaBoardId", nullable = false)
    private QnaBoard qnaBoard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId", nullable = false)
    private Member member;

    @Builder
    public Comment(String commentContent, CommentStatus commentStatus, QnaBoard qnaBoard, Member member) {
        this.commentContent = commentContent;
        this.commentStatus = commentStatus;
        setQnaBoard(qnaBoard);
        setMember(member);
    }

    /**연관관계 편의 메서드*/
    public void setQnaBoard(QnaBoard qnaBoard) {
        this.qnaBoard = qnaBoard;
        if (!qnaBoard.getComments().contains(this)) {
            qnaBoard.addComment(this);
        }
    }

    public void setMember(Member member) {
        this.member = member;
        if (!member.getComments().contains(this)) {
            member.getComments().add(this);
        }
    }

}
