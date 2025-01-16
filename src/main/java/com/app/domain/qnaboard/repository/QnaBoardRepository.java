package com.app.domain.qnaboard.repository;

import com.app.domain.qnaboard.entity.QnaBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QnaBoardRepository extends JpaRepository<QnaBoard, Long> {
}
