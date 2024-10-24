package com.pettory.pettory.counseling.answer.command.domain.repository;


import com.pettory.pettory.counseling.answer.command.domain.aggregate.Answer;

import java.util.Optional;

public interface AnswerRepository {
    Answer save(Answer answer);

    Optional<Answer> findById(Integer counselingAnswerNum);

    void deleteById(Integer counselingAnswerNum);
}
