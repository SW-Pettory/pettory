package com.pettory.pettory.counseling.answer.query.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class AnswerListResponse {
    private List<AnswerDto> answers;
    private int currentPage;
    private int totalPages;
    private long totalItems;
}
