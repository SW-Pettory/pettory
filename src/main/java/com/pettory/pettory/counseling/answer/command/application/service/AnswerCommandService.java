package com.pettory.pettory.counseling.answer.command.application.service;

import com.pettory.pettory.counseling.answer.command.application.dto.AnswerCreateRequest;
import com.pettory.pettory.counseling.answer.command.application.dto.AnswerUpdateRequest;
import com.pettory.pettory.counseling.answer.command.domain.aggregate.Answer;
import com.pettory.pettory.counseling.answer.command.domain.aggregate.AnswerStatus;
import com.pettory.pettory.counseling.answer.command.domain.repository.AnswerRepository;
import com.pettory.pettory.counseling.answer.command.mapper.AnswerMapper;
import com.pettory.pettory.counseling.util.FileUploadUtils;
import com.pettory.pettory.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AnswerCommandService {

    @Value("${image.image-url-ans}")
    private String IMAGE_URL_ANS;
    @Value("${image.image-dir-ans}")
    private String IMAGE_DIR_ANS;

    private final AnswerRepository answerRepository;

    @Transactional
    public Integer createAnswer(AnswerCreateRequest answerRequest, MultipartFile answerImg) {

        String replaceFileName = FileUploadUtils.saveFile(IMAGE_DIR_ANS, answerImg);

        Answer newAnswer = AnswerMapper.toEntity(answerRequest, IMAGE_URL_ANS + replaceFileName);

        Answer answer = answerRepository.save(newAnswer);

        return answer.getCounselingAnswerNum();
    }

    @Transactional
    public void updateAnswer(Integer counselingAnswerNum, AnswerUpdateRequest answerRequest, MultipartFile answerImg) {

        Answer answer = answerRepository.findById(counselingAnswerNum)
                .orElseThrow(() -> new NotFoundException("해당 번호에 맞는 답변이 없습니다. num : " + counselingAnswerNum));

        if(answerImg != null) {
            String replaceFileName = FileUploadUtils.saveFile(IMAGE_DIR_ANS, answerImg);
            FileUploadUtils.deleteFile(IMAGE_DIR_ANS, answer.getCounselingAnswerFileUrl().replace(IMAGE_URL_ANS, ""));
            answer.changeCounselingAnswerFileUrl(IMAGE_URL_ANS + replaceFileName);
        }

        answer.updateAnswerDetails(
                answerRequest.getCounselingQuestionNum(),
                answerRequest.getCounselingAnswerContent(),
                AnswerStatus.valueOf(answerRequest.getCounselingAnswerState()),
                answerRequest.getCounselingAnswerReanswerNum()
        );
    }

    @Transactional
    public void deleteAnswer(Integer counselingAnswerNum) {
        answerRepository.deleteById(counselingAnswerNum);
    }
}
