package com.app.domain.qnaboard.constant;

public enum ContentStatus {

    ACTIVATE, DEACTIVATE;

    public static ContentStatus from(String contentStatus) {
        return ContentStatus.valueOf(contentStatus);
    }
}
