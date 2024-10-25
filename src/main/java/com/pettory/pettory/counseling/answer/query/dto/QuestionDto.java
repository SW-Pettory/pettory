package com.pettory.pettory.counseling.answer.query.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionDto {
    private Integer counselingQuestionNum;
    private String counselingQuestionTitle;
    private String counselingQuestionContent;
    private String counselingQuestionFileUrl;
}
