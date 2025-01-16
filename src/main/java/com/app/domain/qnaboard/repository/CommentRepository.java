package com.app.domain.qnaboard.repository;

import com.app.domain.qnaboard.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
