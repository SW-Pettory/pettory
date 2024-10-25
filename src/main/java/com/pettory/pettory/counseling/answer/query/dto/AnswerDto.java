package com.pettory.pettory.counseling.answer.query.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "답변Query정보 DTO")
public class AnswerDto {
    @Schema(description = "답변번호(PK)")
    private Integer counselingAnswerNum;
    @Schema(description = "질문 번호(FK)")
    private QuestionDto question;
    @Schema(description = "회원 번호")
    private UserDto user;
    @Schema(description = "답변 내용")
    private String counselingAnswerContent;
    @Schema(description = "재답변 번호")
    private Integer counselingAnswerReanswerNum;
    @Schema(description = "답변 파일경로")
    private String counselingAnswerFileUrl;
}
