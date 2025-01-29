package com.app.domain.qnaboard.repository;

import com.app.api.qnaboard.dto.QnaBoardDto;
import com.app.domain.member.entity.QMember;
import com.app.domain.qnaboard.constant.ContentStatus;
import com.app.domain.qnaboard.entity.QAttachment;
import com.app.domain.qnaboard.entity.QQnaBoard;
import com.app.domain.qnaboard.entity.QnaBoard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class QnaBoardRepositoryImpl implements QnaBoardRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    private QnaBoard findQnaBoard(Long qnaBoardId, boolean onlyActive) {
        QQnaBoard qQnaBoard = QQnaBoard.qnaBoard;
        QAttachment qAttachment = QAttachment.attachment;
        QMember qMember = QMember.member;

        var query = queryFactory.selectFrom(qQnaBoard)
                .leftJoin(qQnaBoard.attachments, qAttachment).fetchJoin(); // 항상 attachments 조회

        if (!onlyActive) {
            query.leftJoin(qQnaBoard.member, qMember).fetchJoin(); // onlyActive가 false일 때만 member까지 조회
        }

        query.where(qQnaBoard.qnaBoardId.eq(qnaBoardId));

        if (onlyActive) {
            query.where(qQnaBoard.contentStatus.eq(ContentStatus.ACTIVATE));
        }

        return query.fetchOne();
    }

    @Override
    public QnaBoard findQnaBoardWithDetails(Long qnaBoardId) {
        return findQnaBoard(qnaBoardId, false); // 모든 데이터 조회
    }

    @Override
    public QnaBoard updateQnaBoard(Long qnaBoardId, QnaBoardDto.UpdateRequest updateRequest) {
        QnaBoard qnaBoard = findQnaBoard(qnaBoardId, true); // attachments는 포함, member는 제외

        if (qnaBoard == null) {
            throw new IllegalArgumentException("존재하지 않거나 비활성화된 게시글입니다.");
        }

        qnaBoard.update(updateRequest.getTitle(), updateRequest.getContent());
        return qnaBoard;
    }

    @Override
    public List<QnaBoard> findAllQnaBoardsWithDetails() {
        QQnaBoard qnaBoard = QQnaBoard.qnaBoard;
        QMember member = QMember.member;

        return queryFactory.selectFrom(qnaBoard)
                .leftJoin(qnaBoard.member, member).fetchJoin()
                .where(qnaBoard.contentStatus.eq(ContentStatus.ACTIVATE)) // ACTIVATE 조건 추가
                .fetch();
    }

}
