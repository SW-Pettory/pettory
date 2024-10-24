package com.pettory.pettory.counseling.answer.query.mapper;

import com.pettory.pettory.counseling.answer.query.dto.AnswerDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AnswerMapper {

    List<AnswerDto> selectAnswers(@Param("offset") int offset, @Param("limit") int limit, @Param("counselingAnswerContent") String counselingAnswerContent);

    AnswerDto selectAnswerByNum(@Param("counselingAnswerNum") Integer counselingAnswerNum);

    long countAnswers(@Param("counselingAnswerContent") String counselingAnswerContent);
}
