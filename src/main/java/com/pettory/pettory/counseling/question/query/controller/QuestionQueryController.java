package com.pettory.pettory.counseling.question.query.controller;

import com.pettory.pettory.counseling.question.query.dto.QuestionDetailResponse;
import com.pettory.pettory.counseling.question.query.dto.QuestionListResponse;
import com.pettory.pettory.counseling.question.query.service.QuestionQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "질문 컨트롤러")
@RestController
@RequiredArgsConstructor
@RequestMapping("/question/v1")
@Slf4j
public class QuestionQueryController {

    private final QuestionQueryService questionQueryService;

    @Operation(
            summary = "전체 질문 조회", description = "전체 질문 목록을 조회한다."
    )
    @GetMapping("/questions")
    public ResponseEntity<QuestionListResponse> getQuestions(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String counselingQuestionTitle,
            @RequestParam(required = false) String counselingQuestionContent,
            @RequestParam(required = false) String counselingQuestionTopic,
            @RequestParam(required = false) String userNickname
    ) {

        QuestionListResponse response = questionQueryService.getQuestions(
                page,
                size,
                counselingQuestionTitle,
                counselingQuestionContent,
                counselingQuestionTopic,
                userNickname);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "질문번호로 질문 조회",
            description = "질문번호를 통해 해당하는 질문 정보를 조회한다."
    )
    @GetMapping("/questions/{counselingQuestionNum}")
    public ResponseEntity<QuestionDetailResponse> getQuestion(@PathVariable Integer counselingQuestionNum) {

        QuestionDetailResponse response = questionQueryService.getQuestion(counselingQuestionNum);

        return ResponseEntity.ok(response);
    }

}
