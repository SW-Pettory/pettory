package com.pettory.pettory.counseling.question.query.service;

import com.pettory.pettory.counseling.question.query.dto.QuestionDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class QuestionQueryServiceTest {

    @Autowired
    private QuestionQueryService questionQueryService;

    @Test
    @DisplayName("질문 목록 조회 테스트")
    void testFindAllQuestions() {
        Assertions.assertDoesNotThrow(
                () -> {
                    List<QuestionDto> questions = questionQueryService.getQuestions();
                    questions.forEach(System.out::println);
                }
        );
    }

    @DisplayName("질문 번호별 질문 조회 확인 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"6", "7"})
    void testFindQuestionByNum(int counselingQuestionNum) {
        Assertions.assertDoesNotThrow(
                () -> {
                    QuestionDto question = questionQueryService.findQuestionByNum(counselingQuestionNum);
                    System.out.println(question);
                }
        );
    }

    @DisplayName("질문 제목별 질문 조회 확인 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"신삼", "할거없으면"})
    void testFindQuestionsByTitle(String counselingQuestionTitle) {
        Assertions.assertDoesNotThrow(
                () -> {
                    List<QuestionDto> questions = questionQueryService.findQuestionsByTitle(counselingQuestionTitle);
                    questions.forEach(System.out::println);
                }
        );
    }

    @DisplayName("질문 내용별 질문 조회 확인 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"제가", "호박나이트에서"})
    void testFindQuestionsByContent(String counselingQuestionContent) {
        Assertions.assertDoesNotThrow(
                () -> {
                    List<QuestionDto> questions = questionQueryService.findQuestionsByContent(counselingQuestionContent);
                    questions.forEach(System.out::println);
                }
        );
    }

    @DisplayName("질문 제목+내용별 질문 조회 확인 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"마루"})
    void testFindQuestionsByTopic(String counselingQuestionTopic) {
        Assertions.assertDoesNotThrow(
                () -> {
                    List<QuestionDto> questions = questionQueryService.findQuestionsByTopic(counselingQuestionTopic);
                    questions.forEach(System.out::println);
                }
        );
    }

    @DisplayName("회원 닉네임별 질문 조회 확인 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"민권쓰", "찌유"})
    void testFindQuestionsByNickname(String userNickname) {
        Assertions.assertDoesNotThrow(
                () -> {
                    List<QuestionDto> questions = questionQueryService.findQuestionsByNickname(userNickname);
                    questions.forEach(System.out::println);
                }
        );
    }

}