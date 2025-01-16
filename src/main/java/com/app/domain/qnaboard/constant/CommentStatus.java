package com.app.domain.qnaboard.constant;

public enum CommentStatus {

    ACTIVATE, DEACTIVATE;

    public static CommentStatus from(String commentStatus) {
        return CommentStatus.valueOf(commentStatus);
    }
}
