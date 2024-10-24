package com.pettory.pettory.counseling.question.command.mapper;

import com.pettory.pettory.counseling.question.command.application.dto.QuestionCreateRequest;
import com.pettory.pettory.counseling.question.command.domain.aggregate.Question;

public class QuestionMapper {
    public static Question toEntity(QuestionCreateRequest questionRequest, String fileUrl) {
        return Question.create(
                questionRequest.getUserId(),
                questionRequest.getCounselingQuestionTitle(),
                questionRequest.getCounselingQuestionContent(),
                questionRequest.getCounselingQuestionHits(),
                fileUrl
        );
    }
}
