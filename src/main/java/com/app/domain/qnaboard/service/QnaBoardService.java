package com.app.domain.qnaboard.service;

import com.app.api.qnaboard.dto.QnaBoardDto;
import com.app.domain.qnaboard.entity.Attachment;
import com.app.domain.qnaboard.entity.QnaBoard;
import com.app.domain.qnaboard.repository.QnaBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QnaBoardService {

    private final QnaBoardRepository qnaBoardRepository;

    @Transactional
    public QnaBoard createQnaBoard(QnaBoard qnaBoard) {
        return qnaBoardRepository.save(qnaBoard);
    }

    @Transactional(readOnly = true)
    public List<QnaBoard> findAllQnaBoardsWithDetails() {
        return qnaBoardRepository.findAllQnaBoardsWithDetails();
    }


    @Transactional(readOnly = true)
    public QnaBoard findQnaBoardWithDetails(Long qnaBoardId) {
        QnaBoard qnaBoard = qnaBoardRepository.findQnaBoardWithDetails(qnaBoardId);
        if (qnaBoard == null) {
            throw new IllegalArgumentException("존재하지 않는 게시글입니다.");
        }
        return qnaBoard;
    }

    @Transactional
    public QnaBoardDto.Response updateQnaBoard(Long qnaBoardId, QnaBoardDto.UpdateRequest updateRequest) {
        QnaBoard updatedQnaBoard = qnaBoardRepository.updateQnaBoard(qnaBoardId, updateRequest);

        // 첨부파일 URL만 추출
        List<String> attachmentUrls = updatedQnaBoard.getAttachments().stream()
                .map(Attachment::getUrl)
                .toList();

        // Response DTO로 변환
        return QnaBoardDto.Response.of(updatedQnaBoard, attachmentUrls, updatedQnaBoard.getMember().getMemberName());
    }


    @Transactional
    public void deleteQnaBoard(QnaBoard qnaBoard) {
        qnaBoardRepository.delete(qnaBoard);
    }
}
