package com.pettory.pettory.counseling.question.command.domain.repository;


import com.pettory.pettory.counseling.question.command.domain.aggregate.Question;

import java.util.Optional;

public interface QuestionRepository {
    Question save(Question question);

    Optional<Question> findById(Integer counselingQuestionNum);

    void deleteById(Integer counselingQuestionNum);
}
