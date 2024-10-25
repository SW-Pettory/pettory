package com.pettory.pettory.counseling.answer.query.controller;

import com.pettory.pettory.counseling.answer.query.dto.AnswerDetailResponse;
import com.pettory.pettory.counseling.answer.query.dto.AnswerListResponse;
import com.pettory.pettory.counseling.answer.query.service.AnswerQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "답변 컨트롤러")
@RestController
@RequiredArgsConstructor
@RequestMapping("/answer/v1")
@Slf4j
public class AnswerQueryController {

    private final AnswerQueryService answerQueryService;

    @Operation(
            summary = "전체 답변 조회", description = "전체 답변 목록을 조회한다."
    )
    @GetMapping("/answers")
    public ResponseEntity<AnswerListResponse> getAnswers(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String counselingAnswerContent
    ) {

        AnswerListResponse response = answerQueryService.getAnswers(
                page,
                size,
                counselingAnswerContent);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "답변번호로 답변 조회",
            description = "답변번호를 통해 해당하는 답변 정보를 조회한다."
    )
    @GetMapping("/answers/{counselingAnswerNum}")
    public ResponseEntity<AnswerDetailResponse> getAnswer(@PathVariable Integer counselingAnswerNum) {

        AnswerDetailResponse response = answerQueryService.getAnswer(counselingAnswerNum);

        return ResponseEntity.ok(response);
    }

}
