package com.pettory.pettory.counseling.question.command.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "질문수정Commmand정보 DTO")
public class QuestionUpdateRequest {

    @Min(value = 1)
    @Schema(description = "회원 번호(FK)")
    private final Integer userId;
    @NotBlank
    @Schema(description = "질문 제목")
    private final String counselingQuestionTitle;
    @NotBlank
    @Schema(description = "질문 내용")
    private final String counselingQuestionContent;
    @Min(value = 1)
    @Schema(description = "질문 조회수")
    private final Integer counselingQuestionHits;
    @NotNull
    @Schema(description = "질문 상태")
    private final String counselingQuestionState;

}
