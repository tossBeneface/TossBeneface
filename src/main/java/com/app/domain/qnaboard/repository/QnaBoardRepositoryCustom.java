package com.app.domain.qnaboard.repository;

import com.app.api.qnaboard.dto.QnaBoardDto;
import com.app.domain.qnaboard.entity.QnaBoard;

import java.util.List;

public interface QnaBoardRepositoryCustom {

    List<QnaBoard> findAllQnaBoardsWithDetails();

    QnaBoardDto.Response findQnaBoardWithDetails(Long qnaBoardId);

    QnaBoard updateQnaBoard(Long qnaBoardId, QnaBoardDto.UpdateRequest updateRequest);
}
