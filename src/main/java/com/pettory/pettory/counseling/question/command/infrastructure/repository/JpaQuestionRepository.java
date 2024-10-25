package com.pettory.pettory.counseling.question.command.infrastructure.repository;


import com.pettory.pettory.counseling.question.command.domain.aggregate.Question;
import com.pettory.pettory.counseling.question.command.domain.repository.QuestionRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaQuestionRepository extends QuestionRepository, JpaRepository<Question, Integer> {
}
