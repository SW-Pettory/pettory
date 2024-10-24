package com.pettory.pettory.counseling.answer.command.infrastructure.repository;


import com.pettory.pettory.counseling.answer.command.domain.aggregate.Answer;
import com.pettory.pettory.counseling.answer.command.domain.repository.AnswerRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaAnswerRepository extends AnswerRepository, JpaRepository<Answer, Integer> {
}
