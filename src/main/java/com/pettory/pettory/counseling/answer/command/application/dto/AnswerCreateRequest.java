package com.pettory.pettory.counseling.answer.command.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "답변생성Commmand정보 DTO")
public class AnswerCreateRequest {

    @Min(value = 1)
    @Schema(description = "질문 번호(FK)")
    private final Integer counselingQuestionNum;
    @NotBlank
    @Schema(description = "답변 내용")
    private final String counselingAnswerContent;
    @Min(value = 1)
    @Schema(description = "재답변 번호")
    private final Integer counselingAnswerReanswerNum;

}
