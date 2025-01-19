package com.app.api.qnaboard.dto;

import com.app.domain.qnaboard.constant.ContentStatus;
import com.app.domain.qnaboard.entity.Attachment;
import com.app.domain.qnaboard.entity.QnaBoard;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

public class QnaBoardDto {

    @Getter
    @Setter
    public static class Request {

        @Schema(description = "작성자 ID", example = "1", required = true)
        @NotNull(message = "작성자 ID는 필수 항목입니다.")
        private Long memberId;

        @Schema(description = "제목", example = "상권리포트에 대해 궁금한게 있어요", required = true)
        @NotBlank(message = "제목은 필수 항목입니다.")
        @Size(max = 255, message = "제목은 최대 255자까지 입력할 수 있습니다.")
        private String title;

        @Schema(description = "내용", example = "상권리포트 차트를 보면 이런 이런 내용이 있는데 ...", required = true)
        @NotBlank(message = "내용은 필수 항목입니다.")
        private String content;

        @Schema(description = "첨부파일 리스트", example = ".....", required = false)
        private List<MultipartFile> files;

    }

    @Getter @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {

        @Schema(description = "게시글id", example = "7", required = true)
        private Long qnaBoardId;

        @Schema(description = "제목", example = "상권리포트에 대해 궁금한게 있어요", required = true)
        private String title;

        @Schema(description = "내용", example = "상권리포트 차트를 보면 이런 이런 내용이 있는데 ...", required = true)
        private String content;

        @Schema(description = "작성자 이름", example = "백사장", required = true)
        private String memberName;

        @Schema(description = "게시글상태", example = "ACTIVATE/DEACTIVATE", required = true)
        private ContentStatus contentStatus;

//        @Schema(description = "댓글", example = "그 내용에 대해 답변드리자면...", required = true)
//        private List<Comment> comments;

        @Schema(description = "첨부파일 URL 리스트", example = ".....", required = true)
        private List<String> attachmentUrls;

        public static QnaBoardDto.Response of(QnaBoard qnaBoard) {
            return Response.builder()
                    .title(qnaBoard.getTitle())
                    .content(qnaBoard.getContent())
                    .contentStatus(qnaBoard.getContentStatus())
//                    .comments(qnaBoard.getComments()) // 필요 시 Comment DTO로 변환
                    .attachmentUrls(qnaBoard.getAttachments().stream()
                            .map(Attachment::getUrl) // Attachment의 URL을 추출
                            .collect(Collectors.toList())
                    )
                    .build();
        }
    }
}
