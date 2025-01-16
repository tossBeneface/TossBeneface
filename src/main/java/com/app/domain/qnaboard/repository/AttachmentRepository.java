package com.app.domain.qnaboard.repository;

import com.app.domain.qnaboard.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
}
