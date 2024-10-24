package com.pettory.pettory.counseling.answer.command.application.controller;

import com.pettory.pettory.counseling.answer.command.application.dto.AnswerCreateRequest;
import com.pettory.pettory.counseling.answer.command.application.dto.AnswerUpdateRequest;
import com.pettory.pettory.counseling.answer.command.application.service.AnswerCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;

@Tag(name = "답변 컨트롤러")
@RestController
@RequiredArgsConstructor
@RequestMapping("/answer/v1")
@Slf4j
public class AnswerCommandController {

    private final AnswerCommandService answerCommandService;

    @Operation(summary = "신규 답변 등록", description = "신규 답변을 등록한다.")
    @PostMapping("/answers")
    public ResponseEntity<Void> createAnswer(
            @RequestPart @Valid AnswerCreateRequest answerRequest,
            @RequestPart(required = false) MultipartFile answerImg
    ) {

        Integer counselingAnswerNum = answerCommandService.createAnswer(answerRequest, answerImg);

        return ResponseEntity
                .created(URI.create("/answer/v1/answers/" + counselingAnswerNum))
                .build();
    }

    @Operation(summary = "답변번호로 답변 수정", description = "답변번호를 통해 해당하는 답변 정보를 수정한다.")
    @PutMapping("/answers/{counselingAnswerNum}")
    public ResponseEntity<Void> updateAnswer(
            @PathVariable Integer counselingAnswerNum,
            @RequestPart @Valid AnswerUpdateRequest answerRequest,
            @RequestPart(required = false) MultipartFile answerImg
    ) {

        answerCommandService.updateAnswer(counselingAnswerNum, answerRequest, answerImg);

        return ResponseEntity.created(URI.create("/answer/v1/answers/" + counselingAnswerNum)).build();
    }

    @Operation(summary = "답변번호로 답변 삭제", description = "답변번호를 통해 해당하는 답변 정보를 삭제한다.")
    @DeleteMapping("/answers/{counselingAnswerNum}")
    public ResponseEntity<Void> deleteAnswer(@PathVariable final Integer counselingAnswerNum) {

        answerCommandService.deleteAnswer(counselingAnswerNum);

        return ResponseEntity.noContent().build();
    }
}
