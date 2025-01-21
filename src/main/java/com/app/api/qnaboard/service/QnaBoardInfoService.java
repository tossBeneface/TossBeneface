package com.app.api.qnaboard.service;

import com.app.api.qnaboard.dto.QnaBoardDto;
import com.app.domain.member.entity.Member;
import com.app.domain.member.service.MemberService;
import com.app.domain.qnaboard.constant.ContentStatus;
import com.app.domain.qnaboard.entity.QnaBoard;
import com.app.domain.qnaboard.entity.Attachment;
import com.app.domain.qnaboard.service.AttachmentService;
import com.app.domain.qnaboard.service.QnaBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class QnaBoardInfoService {

    private final QnaBoardService qnaBoardService;
    private final MemberService memberService;
    private final AttachmentService attachmentService;

    @Transactional
    public Long createQnaBoard(QnaBoardDto.Request requestDto) {
        Member member = memberService.findMemberById(requestDto.getMemberId());

        /// 첨부파일 없이 QnaBoard 저장
        QnaBoard qnaBoard = QnaBoard.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .attachments(new ArrayList<>())
                .member(member)
                .contentStatus(ContentStatus.ACTIVATE)
                .build();
        QnaBoard savedBoard = qnaBoardService.createQnaBoard(qnaBoard);

        if (requestDto.getFiles() != null && !requestDto.getFiles().isEmpty()) {
            attachmentService.saveAttachments(requestDto.getFiles(), savedBoard);
        }

        // QnaBoard 저장 및 ID 반환
        return qnaBoard.getQnaBoardId();
    }

    @Transactional(readOnly = true)
    public QnaBoardDto.Response getQnaBoardById(Long qnaBoardId) {
        QnaBoard qnaBoard = qnaBoardService.findQnaBoardWithDetails(qnaBoardId);

        List<String> attachmentUrls = qnaBoard.getAttachments().stream()
                .map(Attachment::getUrl)
                .toList();

        return QnaBoardDto.Response.of(qnaBoard, attachmentUrls, qnaBoard.getMember().getMemberName());
    }


    @Transactional(readOnly = true)
    public List<QnaBoardDto.Summary> getQnaBoardSummaries() {
        List<QnaBoard> qnaBoards = qnaBoardService.findAllQnaBoardsWithDetails();

        return qnaBoards.stream()
                .map(qnaBoard -> {
                    String authorName = qnaBoard.getMember() != null ? qnaBoard.getMember().getMemberName() : "알 수 없음";
                    boolean hasComments = qnaBoard.getComments() != null && !qnaBoard.getComments().isEmpty();

                    return new QnaBoardDto.Summary(
                            qnaBoard.getQnaBoardId(),
                            qnaBoard.getTitle(),
                            authorName,
                            qnaBoard.getCreatedAt(),
                            hasComments
                    );
                })
                .toList();
    }

    @Transactional
    public Long updateQnaBoard(Long qnaBoardId, QnaBoardDto.UpdateRequest updateRequest, List<MultipartFile> files) {
        // QnaBoard 조회
        QnaBoard qnaBoard = qnaBoardService.findQnaBoardWithDetails(qnaBoardId);
        if (qnaBoard == null) {
            throw new IllegalArgumentException("존재하지 않는 게시글입니다.");
        }

        qnaBoard.update(updateRequest.getTitle(), updateRequest.getContent());

        // 기존 첨부파일 삭제 및 새로운 파일 저장
        if (files != null && !files.isEmpty()) {
            attachmentService.deleteAttachmentsByQnaBoard(qnaBoard);
            attachmentService.saveAttachments(files, qnaBoard);
        }

        return qnaBoard.getQnaBoardId();
    }


    // 잘 처리됐다 라는 response를 돌려줘야 (공통 response작성)
    public void deleteQnaBoard(Long qnaBoardId) {
        // status처리
    }
}
