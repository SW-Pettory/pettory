package com.pettory.pettory.counseling.question.query.service;

import com.pettory.pettory.counseling.question.query.dto.QuestionDetailResponse;
import com.pettory.pettory.counseling.question.query.dto.QuestionDto;
import com.pettory.pettory.counseling.question.query.dto.QuestionListResponse;
import com.pettory.pettory.counseling.question.query.mapper.QuestionMapper;
import com.pettory.pettory.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionQueryService {

    private final QuestionMapper questionMapper;

    @Transactional(readOnly = true)
    public QuestionListResponse getQuestions(Integer page, Integer size, String counselingQuestionTitle, String counselingQuestionContent, String counselingQuestionTopic, String userNickname) {
        int offset = (page - 1) * size;
        List<QuestionDto> questions = questionMapper.selectQuestions(offset, size, counselingQuestionTitle, counselingQuestionContent, counselingQuestionTopic, userNickname);

        long totalItems = questionMapper.countQuestions(counselingQuestionTitle, counselingQuestionContent, counselingQuestionTopic, userNickname);

        return QuestionListResponse.builder()
                .questions(questions)
                .currentPage(page)
                .totalPages((int) Math.ceil((double) totalItems / size))
                .totalItems(totalItems)
                .build();
    }

    @Transactional(readOnly = true)
    public QuestionDetailResponse getQuestion(Integer counselingQuestionNum) {
        QuestionDto question = questionMapper.selectQuestionByNum(counselingQuestionNum);

        if (question == null) {
            throw new NotFoundException("해당 번호를 가진 질문을 찾지 못했습니다. 질문 번호 : " + counselingQuestionNum);
        }

        return new QuestionDetailResponse(question);
    }

}
