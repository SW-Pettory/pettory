package com.pettory.pettory.counseling.question.command.application.service;

import com.pettory.pettory.counseling.question.command.application.dto.QuestionCreateRequest;
import com.pettory.pettory.counseling.question.command.application.dto.QuestionUpdateRequest;
import com.pettory.pettory.counseling.question.command.domain.aggregate.Question;
import com.pettory.pettory.counseling.question.command.domain.aggregate.QuestionStatus;
import com.pettory.pettory.counseling.question.command.domain.repository.QuestionRepository;
import com.pettory.pettory.counseling.question.command.mapper.QuestionMapper;
import com.pettory.pettory.counseling.util.FileUploadUtils;
import com.pettory.pettory.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class QuestionCommandService {

    @Value("${image.image-url-que}")
    private String IMAGE_URL_QUE;
    @Value("${image.image-dir-que}")
    private String IMAGE_DIR_QUE;

    private final QuestionRepository questionRepository;

    @Transactional
    public Integer createQuestion(QuestionCreateRequest questionRequest, MultipartFile questionImg) {

        String replaceFileName = FileUploadUtils.saveFile(IMAGE_DIR_QUE, questionImg);

        Question newQuestion = QuestionMapper.toEntity(questionRequest, IMAGE_URL_QUE + replaceFileName);

        Question question = questionRepository.save(newQuestion);

        return question.getCounselingQuestionNum();
    }

    @Transactional
    public void updateQuestion(Integer counselingQuestionNum, QuestionUpdateRequest questionRequest, MultipartFile questionImg) {

        Question question = questionRepository.findById(counselingQuestionNum)
                .orElseThrow(() -> new NotFoundException("해당 번호에 맞는 질문이 없습니다. num : " + counselingQuestionNum));

        if(questionImg != null) {
            String replaceFileName = FileUploadUtils.saveFile(IMAGE_DIR_QUE, questionImg);
            FileUploadUtils.deleteFile(IMAGE_DIR_QUE, question.getCounselingQuestionFileUrl().replace(IMAGE_URL_QUE, ""));
            question.changeCounselingQuestionFileUrl(IMAGE_URL_QUE + replaceFileName);
        }

        question.updateQuestionDetails(
                questionRequest.getUserId(),
                questionRequest.getCounselingQuestionTitle(),
                questionRequest.getCounselingQuestionContent(),
                questionRequest.getCounselingQuestionHits(),
                QuestionStatus.valueOf(questionRequest.getCounselingQuestionState())
        );
    }

    @Transactional
    public void deleteQuestion(Integer counselingQuestionNum) {
        questionRepository.deleteById(counselingQuestionNum);
    }
}
