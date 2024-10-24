package com.pettory.pettory.counseling.question.query.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "질문Query정보 DTO")
public class QuestionDto {
    @Schema(description = "질문번호(PK)")
    private Integer counselingQuestionNum;
    @Schema(description = "회원 번호(FK)")
    private UserDto user;
    @Schema(description = "질문 제목")
    private String counselingQuestionTitle;
    @Schema(description = "질문 내용")
    private String counselingQuestionContent;
    @Schema(description = "질문 조회수")
    private Integer counselingQuestionHits;
    @Schema(description = "질문 파일경로")
    private String counselingQuestionFileUrl;
}
