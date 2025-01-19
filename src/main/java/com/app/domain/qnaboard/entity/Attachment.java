package com.app.domain.qnaboard.entity;

import com.app.domain.common.BaseEntity;
import com.app.domain.qnaboard.constant.FileStatus;
import com.app.global.util.FileUtils;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Attachment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attachmentId;

    @Column(nullable = false, length = 500)
    private String url;  // 외부에서 접근 가능한 S3 URL

    // TODO 어떻게 사용해야할지 생각해보기
    @Column(nullable = true, length = 500)
    private String filePath;

    @Column(nullable = false, length = 50)
    private String fileType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private FileStatus fileStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "qnaBoardId", nullable = false)
    private QnaBoard qnaBoard;

    @Builder
    public Attachment(String url, String filePath, String fileType, FileStatus fileStatus, QnaBoard qnaBoard) {
        this.url = url;
        this.filePath = filePath;
        this.fileType = fileType;
        this.fileStatus = fileStatus;
        setQnaBoard(qnaBoard);
    }

    /**연관관계 편의 메서드*/
    public void setQnaBoard(QnaBoard qnaBoard) {
        this.qnaBoard = qnaBoard;
        if (!qnaBoard.getAttachments().contains(this)) {
            qnaBoard.addAttachment(this);
        }
    }

    public Attachment(String url, QnaBoard qnaBoard) {
        this.url = url;
        this.qnaBoard = qnaBoard;
    }

    public void updateFile(String newFileUrl) {
        this.url = newFileUrl;
        this.filePath =  FileUtils.extractFileNameFromUrl(newFileUrl);// URL에서 파일 이름 추출 로직
        this.fileStatus = FileStatus.ACTIVATE;
    }

}
