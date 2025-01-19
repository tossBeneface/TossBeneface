package com.app.domain.qnaboard.repository;

import com.app.api.qnaboard.dto.QnaBoardDto;
import com.app.domain.member.entity.QMember;
import com.app.domain.qnaboard.entity.QAttachment;
import com.app.domain.qnaboard.entity.QQnaBoard;
import com.app.domain.qnaboard.entity.QnaBoard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class QnaBoardRepositoryImpl implements QnaBoardRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<QnaBoard> findAllQnaBoardsWithDetails() {
        QQnaBoard qnaBoard = QQnaBoard.qnaBoard;
        QMember member = QMember.member;

        return queryFactory.selectFrom(qnaBoard)
                .leftJoin(qnaBoard.member, member).fetchJoin()
                .fetch();
    }

    @Override
    public QnaBoard findQnaBoardWithDetails(Long qnaBoardId) {
        QQnaBoard qQnaBoard = QQnaBoard.qnaBoard;
        QAttachment qAttachment = QAttachment.attachment;
        QMember qMember = QMember.member;

        // QueryDSL로 데이터 조회
        return queryFactory
                .selectFrom(qQnaBoard)
                .leftJoin(qQnaBoard.attachments, qAttachment).fetchJoin()
                .leftJoin(qQnaBoard.member, qMember).fetchJoin()
                .where(qQnaBoard.qnaBoardId.eq(qnaBoardId))
                .fetchOne();
    }

    @Override
    public QnaBoard updateQnaBoard(Long qnaBoardId, QnaBoardDto.UpdateRequest updateRequest) {
        QQnaBoard qQnaBoard = QQnaBoard.qnaBoard;
        // 기존 QnaBoard를 조회
        QnaBoard qnaBoard = queryFactory
                .selectFrom(qQnaBoard)
                .where(qQnaBoard.qnaBoardId.eq(qnaBoardId))
                .fetchOne();
        if (qnaBoard == null) {
            throw new IllegalArgumentException("존재하지 않는 게시글입니다.");
        }
        // QnaBoard 수정
        qnaBoard.update(updateRequest.getTitle(), updateRequest.getContent());
        return qnaBoard;
    }
}
