package com.app.domain.qnaboard.repository;

import com.app.domain.qnaboard.entity.QnaBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface QnaBoardRepository extends JpaRepository<QnaBoard, Long> {

    @Query("SELECT qb FROM QnaBoard qb " +
            "LEFT JOIN FETCH qb.attachments " +
            "LEFT JOIN FETCH qb.member " +
            "WHERE qb.qnaBoardId = :qnaBoardId")
    Optional<QnaBoard> findByIdWithDetails(@Param("qnaBoardId") Long qnaBoardId);
}
