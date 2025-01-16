package com.app.api.qnaboard.service;

import com.app.api.file.service.FileUploadService;
import com.app.api.qnaboard.dto.QnaBoardDto;
import com.app.domain.member.entity.Member;
import com.app.domain.member.service.MemberService;
import com.app.domain.qnaboard.constant.ContentStatus;
import com.app.domain.qnaboard.entity.Attachment;
import com.app.domain.qnaboard.entity.QnaBoard;
import com.app.domain.qnaboard.service.QnaBoardDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QnaBoardService {

    private final QnaBoardDomainService qnaBoardDomainService;
    private final MemberService memberService;
    private final FileUploadService fileUploadService;

    @Transactional
    public Long createQnaBoard(QnaBoardDto.Request requestDto) {
        Member member = memberService.findMemberById(requestDto.getMemberId());

        // 파일 업로드 및 Attachment 엔티티 리스트 생성
        List<Attachment> attachments = fileUploadService.uploadFiles(requestDto.getFiles());

        // QnaBoard 엔티티 생성
        QnaBoard qnaBoard = QnaBoard.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .member(member)
                .attachments(attachments) // 업로드된 첨부파일 리스트 설정
                .contentStatus(ContentStatus.ACTIVATE)
                .build();

        // QnaBoard 저장 및 ID 반환
        return qnaBoardDomainService.createQnaBoard(qnaBoard).getQnaBoardId();
    }

    @Transactional(readOnly = true)
    public QnaBoardDto.Response getQnaBoardById(Long qnaBoardId) {
        QnaBoard qnaBoard = qnaBoardDomainService.findQnaBoardById(qnaBoardId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        return QnaBoardDto.Response.of(qnaBoard);
    }

    @Transactional(readOnly = true)
    public List<QnaBoardDto.Response> getAllQnaBoards() {
//        return qnaBoardDomainService.findQnaBoards()
//                .stream()
//                .map(QnaBoardDto.Response::of)
//                .collect();
        return null;
    }

    public QnaBoardDto.Response updateQnaBoard(Long qnaBoardId, QnaBoardDto.Request requestDto) {
        return null;
    }

    // 잘 처리됐다 라는 response를 돌려줘야 (공통 response작성)
    public void deleteQnaBoard(Long qnaBoardId) {
        // status처리
    }
}
