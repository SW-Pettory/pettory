package com.pettory.pettory.counseling.question.query.mapper;

import com.pettory.pettory.counseling.question.query.dto.QuestionDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface QuestionMapper {

    List<QuestionDto> selectQuestions(@Param("offset") int offset, @Param("limit") int limit, @Param("counselingQuestionTitle") String counselingQuestionTitle, @Param("counselingQuestionContent") String counselingQuestionContent, @Param("counselingQuestionTopic") String counselingQuestionTopic, @Param("userNickname") String userNickname);

    QuestionDto selectQuestionByNum(@Param("counselingQuestionNum") Integer counselingQuestionNum);

    long countQuestions(@Param("counselingQuestionTitle") String counselingQuestionTitle, @Param("counselingQuestionContent") String counselingQuestionContent, @Param("counselingQuestionTopic") String counselingQuestionTopic, @Param("userNickname") String userNickname);
}
