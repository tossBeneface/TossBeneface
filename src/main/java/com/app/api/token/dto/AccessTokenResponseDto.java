package com.app.api.token.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.Builder;
import lombok.Getter;

@Getter @Builder
public class AccessTokenResponseDto {

    @Schema(description = "grantType", example = "Bearer", required = true)
    private String grantType;

    @Schema(description = "accessToken", example = "eyJ0eXBlIjoiSldUIiwiYWxnIjoiSFM1MTIifQ.eyJzdWIiOiJBQ0NFU1MiLCJpYXQiOjE3MTEyMDI1OTQsImV4cCI6MTcxMTIwMzQ5NCwibWVtYmVySWQiOjEsInJvbGUiOiJBRE1JTiJ9.DJxNS5CQes5Q2_L11N0Fxo_yPc06s8HEtZdDiRQBJzZDXNHfu5o699_4NfLk8K_xrqPtE54SWNltZ68rl77_6w", required = true)
    private String accessToken;

    @Schema(description = "access token 만료 시간", example = "2024-03-23 23:18:14", required = true)
    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date accessTokenExpireTime;
}
