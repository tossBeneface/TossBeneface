package com.app.domain.qnaboard.service;

import com.app.domain.qnaboard.entity.QnaBoard;
import com.app.domain.qnaboard.repository.QnaBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QnaBoardService {

    private final QnaBoardRepository qnaBoardRepository;


    @Transactional
    public QnaBoard createQnaBoard(QnaBoard qnaBoard) {
        return qnaBoardRepository.save(qnaBoard);
    }

    @Transactional(readOnly = true)
    public Optional<QnaBoard> findQnaBoardById(Long qnaBoardId) {
        return qnaBoardRepository.findById(qnaBoardId);
    }

    @Transactional(readOnly = true)
    public List<QnaBoard> findQnaBoards() {
        return qnaBoardRepository.findAll();
    }

    @Transactional
    public void updateQnaBoard(QnaBoard qnaBoard, String newTitle, String newContent) {
        if ((newTitle == null || newTitle.isEmpty()) && (newContent == null || newContent.isEmpty())) {
            throw new IllegalArgumentException("제목과 내용 중 하나는 반드시 입력해야 합니다.");
        }
        qnaBoard.update(newTitle, newContent);
    }

    @Transactional
    public void deleteQnaBoard(QnaBoard qnaBoard) {
        qnaBoardRepository.delete(qnaBoard);
    }
}
