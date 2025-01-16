package com.app.domain.qnaboard.constant;

public enum FileStatus {

    ACTIVATE, DEACTIVATE;

    public static FileStatus from(String fileStatus) {
        return FileStatus.valueOf(fileStatus);
    }
}
