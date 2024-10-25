package com.pettory.pettory.counseling.question.query.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class QuestionListResponse {
    private List<QuestionDto> questions;
    private int currentPage;
    private int totalPages;
    private long totalItems;
}
