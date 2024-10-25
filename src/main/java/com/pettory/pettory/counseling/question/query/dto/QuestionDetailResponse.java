package com.pettory.pettory.counseling.question.query.dto;

import lombok.Getter;

@Getter
public class QuestionDetailResponse {
    private QuestionDto question;

    public QuestionDetailResponse(QuestionDto question) {
        this.question = question;
    }

}
