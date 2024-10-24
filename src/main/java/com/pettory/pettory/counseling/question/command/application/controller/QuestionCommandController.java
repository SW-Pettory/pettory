package com.pettory.pettory.counseling.question.command.application.controller;

import com.pettory.pettory.counseling.question.command.application.dto.QuestionCreateRequest;
import com.pettory.pettory.counseling.question.command.application.dto.QuestionUpdateRequest;
import com.pettory.pettory.counseling.question.command.application.service.QuestionCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;

@Tag(name = "질문 컨트롤러")
@RestController
@RequiredArgsConstructor
@RequestMapping("/question/v1")
@Slf4j
public class QuestionCommandController {

    private final QuestionCommandService questionCommandService;

    @Operation(summary = "신규 질문 등록", description = "신규 질문을 등록한다.")
    @PostMapping("/questions")
    public ResponseEntity<Void> createQuestion(
            @RequestPart @Valid QuestionCreateRequest questionRequest,
            @RequestPart(required = false) MultipartFile questionImg
    ) {

        Integer counselingQuestionNum = questionCommandService.createQuestion(questionRequest, questionImg);

        return ResponseEntity
                .created(URI.create("/question/v1/questions/" + counselingQuestionNum))
                .build();
    }

    @Operation(summary = "질문번호로 질문 수정", description = "질문번호를 통해 해당하는 질문 정보를 수정한다.")
    @PutMapping("/questions/{counselingQuestionNum}")
    public ResponseEntity<Void> updateQuestion(
            @PathVariable Integer counselingQuestionNum,
            @RequestPart @Valid QuestionUpdateRequest questionRequest,
            @RequestPart(required = false) MultipartFile questionImg
    ) {

        questionCommandService.updateQuestion(counselingQuestionNum, questionRequest, questionImg);

        return ResponseEntity.created(URI.create("/question/v1/questions/" + counselingQuestionNum)).build();

    }

    @Operation(summary = "질문번호로 질문 삭제", description = "질문번호를 통해 해당하는 질문 정보를 삭제한다.")
    @DeleteMapping("/questions/{counselingQuestionNum}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable final Integer counselingQuestionNum) {

        questionCommandService.deleteQuestion(counselingQuestionNum);

        return ResponseEntity.noContent().build();
    }
}
