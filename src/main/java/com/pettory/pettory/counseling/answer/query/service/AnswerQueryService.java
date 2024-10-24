package com.pettory.pettory.counseling.answer.query.service;

import com.pettory.pettory.counseling.answer.query.dto.AnswerDetailResponse;
import com.pettory.pettory.counseling.answer.query.dto.AnswerDto;
import com.pettory.pettory.counseling.answer.query.dto.AnswerListResponse;
import com.pettory.pettory.counseling.answer.query.mapper.AnswerMapper;
import com.pettory.pettory.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnswerQueryService {

    private final AnswerMapper answerMapper;

    @Transactional(readOnly = true)
    public AnswerListResponse getAnswers(Integer page, Integer size, String counselingAnswerContent) {
        int offset = (page - 1) * size;
        List<AnswerDto> answers = answerMapper.selectAnswers(offset, size, counselingAnswerContent);

        long totalItems = answerMapper.countAnswers(counselingAnswerContent);

        return AnswerListResponse.builder()
                .answers(answers)
                .currentPage(page)
                .totalPages((int) Math.ceil((double) totalItems / size))
                .totalItems(totalItems)
                .build();
    }

    @Transactional(readOnly = true)
    public AnswerDetailResponse getAnswer(Integer counselingAnswerNum) {
        AnswerDto answer = answerMapper.selectAnswerByNum(counselingAnswerNum);

        if (answer == null) {
            throw new NotFoundException("해당 번호를 가진 답변을 찾지 못했습니다. 답변 번호 : " + counselingAnswerNum);
        }

        return new AnswerDetailResponse(answer);
    }

}
