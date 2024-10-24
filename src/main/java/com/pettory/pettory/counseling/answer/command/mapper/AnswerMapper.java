package com.pettory.pettory.counseling.answer.command.mapper;

import com.pettory.pettory.counseling.answer.command.application.dto.AnswerCreateRequest;
import com.pettory.pettory.counseling.answer.command.domain.aggregate.Answer;

public class AnswerMapper {
    public static Answer toEntity(AnswerCreateRequest answerRequest, String fileUrl) {
        return Answer.create(
                answerRequest.getCounselingQuestionNum(),
                answerRequest.getCounselingAnswerContent(),
                answerRequest.getCounselingAnswerReanswerNum(),
                fileUrl
        );
    }
}
