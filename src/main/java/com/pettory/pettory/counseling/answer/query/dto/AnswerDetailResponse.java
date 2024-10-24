package com.pettory.pettory.counseling.answer.query.dto;

import lombok.Getter;

@Getter
public class AnswerDetailResponse {
    private AnswerDto answer;

    public AnswerDetailResponse(AnswerDto answer) {
        this.answer = answer;
    }

}
